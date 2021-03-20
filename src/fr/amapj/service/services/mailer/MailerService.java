/*
 *  Copyright 2013-2050 Emmanuel BRUN (contact@amapj.fr)
 * 
 *  This file is part of AmapJ.
 *  
 *  AmapJ is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  AmapJ is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with AmapJ.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
 package fr.amapj.service.services.mailer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.acces.RoleList;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.fichierbase.ExpediteurAmap;
import fr.amapj.model.models.fichierbase.HistoriqueEmail;
import fr.amapj.model.models.fichierbase.ModeleEmail;
import fr.amapj.model.models.param.ModeleEmailEnum;
import fr.amapj.model.models.param.Parametres;
import fr.amapj.service.engine.tools.DbToDto;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.producteur.ProducteurDTO;
import fr.amapj.view.engine.ui.AppConfiguration;

/**
 * Permet d'envoyer des mails
 * 
 *
 */
public class MailerService
{
	private final static Logger logger = LogManager.getLogger();


	public MailerService()
	{

	}
	
	private Message initMail(ParametresDTO param, String recipient, String subject) throws AddressException, MessagingException, UnsupportedEncodingException
	{
		
		
		Session session;
		switch (param.smtpType)
		{
		case GMAIL:
			session = getGmailSession(param);
			break;
		
		case POSTFIX_LOCAL :
			session = getPostfixLocalSession(param);
			break;

		default:
			throw new AmapjRuntimeException();
		}
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(param.sendingMailUsername,param.nomAmap));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
		if ( (param.mailCopyTo!=null) && (param.mailCopyTo.length()>0))
		{
			message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(param.mailCopyTo));
			// Supprimé - Provoque le rejet du message en tant que SPAM - message.setReplyTo(InternetAddress.parse(param.mailCopyTo));
		}
		message.setSubject(subject);
		
		return message;
	}
	
	
	
	private Session getPostfixLocalSession(ParametresDTO param)
	{
		final String username = param.getSendingMailUsername();
		
		if ( (username==null) || (username.length()==0))
		{
			throw new AmapjRuntimeException("Le service mail n'est pas paramétré : absence du nom de l'utilisateur");
		}
		
		Properties props = new Properties();
		
		props.put("mail.smtp.host", "127.0.0.1");
		Session	session = Session.getInstance(props);

		return session;
	}

	private Session getGmailSession(ParametresDTO param)
	{
		final String username = param.getSendingMailUsername();
		final String password = param.getSendingMailPassword();
		
		if ( (username==null) || (username.length()==0))
		{
			throw new AmapjRuntimeException("Le service mail n'est pas paramétré correctement: absence du nom de l'utilisateur");
		}
		
		if ( (password==null) || (password.length()==0))
		{
			throw new AmapjRuntimeException("Le service mail n'est pas paramétré correctement: absence du mot de passe");
		}
		
		Properties props = new Properties();
		
	
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
			
		Session session = Session.getInstance(props, new javax.mail.Authenticator()
			{
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(username, password);
				}
			});
		
		return session;
	}

	/**
	 *  Envoi d'un mail 
	 */
	public void sendHtmlMail(MailerMessage mailerMessage)
	{
	
		ParametresDTO param = new ParametresService().getParametres();
		
		if (MailerCounter.isAllowed(param)==false)	
		{
			throw new AmapjRuntimeException("Impossible d'envoyer un mail car le quota par jour est dépassé (quota = "+param.getSendingMailNbMax()+" )");
		}
		
		try
		{
			Message message = initMail(param,mailerMessage.getEmail(), mailerMessage.getTitle());
			
			Multipart mp = new MimeMultipart();

	        MimeBodyPart htmlPart = new MimeBodyPart();
	        String html = buildHtlmContent(mailerMessage,param);
	        htmlPart.setText(html, "UTF-8", "html");
	        mp.addBodyPart(htmlPart);

	        for (MailerAttachement attachement : mailerMessage.getAttachements())
			{
		        MimeBodyPart attachment = new MimeBodyPart();
		        attachment.setFileName(attachement.getName());
		        attachment.setDataHandler(new DataHandler(attachement.getDataSource()));
		        mp.addBodyPart(attachment);
	        }
			
			message.setContent(mp);
			
			if (AppConfiguration.getConf().isAllowMailControl()==false)
			{
				// Le cas standard : le mail est envoyé 
				Transport.send(message);
				logger.info("Envoi d'un message a : "+mailerMessage.getEmail());
			}
			else
			{
				// Le cas debug : le mail est stocké 
				MailerStorage.store(message);
				logger.info("STOCKAGE d'un message destiné a : "+mailerMessage.getEmail());
			}
		}
		catch (MessagingException | UnsupportedEncodingException e)
		{
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	
	@DbRead
	public List<HistoriqueEmailDTO> getHistoriqueMail(List<RoleList> roles, ProducteurDTO producteur) {
		StringBuffer roleList = new StringBuffer();
		for( RoleList role : roles ) {
			if( roleList.length()!=0 )
				roleList.append(",");
			roleList.append("\""+role.name()+"\"");
		}
		List<HistoriqueEmailDTO> res = null;
		String whereClause = "";
		if( roles.contains(RoleList.ADMIN) || roles.contains(RoleList.TRESORIER) || roles.contains(RoleList.REFERENT)) {
			whereClause = "where hm.roleExpediteur in ("+roleList+")";
		} else if (roles.contains(RoleList.PRODUCTEUR) && producteur != null && producteur.getEmailContact() != null) {
			whereClause = "where hm.adresseExpediteur=\""+producteur.getEmailContact()+"\" or (hm.roleExpediteur=\"PRODUCTEUR\" and hm.expediteurAmap!=null)";
		} else { // Au cas où on arriverait là sans le rôle adéquat
			whereClause = " where 1=2";
		}
		try {
			EntityManager em = TransactionHelper.getEm();
			Query q = em.createQuery("select hm from HistoriqueEmail hm "+whereClause);
			HistoriqueEmailService service = new HistoriqueEmailService();
			res= DbToDto.transform(q, (HistoriqueEmail hm)->service.createHistoriqueEmailDto(em, hm));
		} catch(Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	@DbRead
	public static ModeleEmail getModeleEmail(ModeleEmailEnum designation) {
		if( designation == null )
			return null;
		
		EntityManager em = TransactionHelper.getEm();
		Query q = em.createQuery("select m from ModeleEmail m where m.designation=:designation");
		q.setParameter("designation", designation);
		List<ModeleEmail> res = q.getResultList();
		if( res != null && res.size()>0 )
			return res.get(0);
		return null;
	}
	
	@DbRead
	public static List<ModeleEmail> getAllModeleEmail() {
		EntityManager em = TransactionHelper.getEm();
		Query q = em.createQuery("select m from ModeleEmail m");
		List<ModeleEmail> res = q.getResultList();
		return res;
	}
	
	@DbRead
	public static ModeleEmailDTO getModeleEmailDTO(Long id) {
		EntityManager em = TransactionHelper.getEm();

		ModeleEmail me = em.find(ModeleEmail.class, id);
		if( me != null ) {
			return modeleEmail2DTO(me);
		}
		return null;
	}
	
	@DbRead
	public static ModeleEmailDTO getModeleEmailDTO(ModeleEmailEnum des) {
		ModeleEmail me = getModeleEmail(des);

		return modeleEmail2DTO(me);
	}
	
	private static ModeleEmailDTO modeleEmail2DTO( ModeleEmail me ) {
		if( me != null ) {
			ModeleEmailDTO it = new ModeleEmailDTO();
			it.setContenuEmail(me.getContenu());
			it.setDesignation(me.getDesignation());
			it.setTitreEmail(me.getTitre());
			it.setId(me.getId());
			return it;
		}
		return null;
	}
	
	@DbWrite
	public static void updateModeleEmail(ModeleEmailDTO med ) {
		EntityManager em = TransactionHelper.getEm();
		
		ModeleEmail me = em.find(ModeleEmail.class, med.getId());
		
		me.setContenu(med.getContenuEmail());
		me.setTitre(med.getTitreEmail());

	}
	
	@DbRead
	public static ExpediteurAmap getExpediteurAmap(Long id) {
		if( id == null )
			return null;
		
		EntityManager em = TransactionHelper.getEm();

		return em.find(ExpediteurAmap.class, id);
	}
	
	@DbWrite
	public static void saveMail(HistoriqueEmail message) {
		EntityManager em = TransactionHelper.getEm();
		em.persist(message);
	}
	
	@DbWrite
	public static void deleteEmail(Long id) {
		EntityManager em = TransactionHelper.getEm();
		try {
			HistoriqueEmail hm = em.find(HistoriqueEmail.class, id);
			em.remove(hm);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private String buildHtlmContent(MailerMessage mailerMessage, ParametresDTO param)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append(HTML_MAIL_HEADER);
		
		sb.append(mailerMessage.getContent());
		if (param.sendingMailFooter!=null && param.sendingMailFooter.length()>0)
		{
			sb.append("<br/><br/>");
			sb.append(param.sendingMailFooter);
		}
		
		sb.append(HTML_MAIL_FOOTER);
		
		return sb.toString();
	}

	public static void main(String[] args)
	{
		TestTools.init();
		
		MailerMessage message = new MailerMessage("essai@gmail.com", "essai", "<h1>This is actual message</h1>");
		
		new MailerService().sendHtmlMail(message);
	}
}
