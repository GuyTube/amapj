/*
 *  Copyright 2013-2018 Emmanuel BRUN (contact@amapj.fr)
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
 package fr.amapj.service.services.notification;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.amapj.common.DateUtils;
import fr.amapj.common.Dictionary;
import fr.amapj.common.DictionaryEnum;
import fr.amapj.common.StackUtils;
import fr.amapj.model.engine.transaction.Call;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.NewTransaction;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.contrat.modele.EtatModeleContrat;
import fr.amapj.model.models.fichierbase.ModeleEmail;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.model.models.param.ModeleEmailEnum;
import fr.amapj.model.models.stats.NotificationDone;
import fr.amapj.model.models.stats.TypNotificationDone;
import fr.amapj.service.engine.deamons.DeamonsContext;

import fr.amapj.service.services.mailer.MailerMessage;
import fr.amapj.service.services.mailer.MailerService;
import fr.amapj.service.services.meslivraisons.JourLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.MesLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.MesLivraisonsService;
import fr.amapj.service.services.meslivraisons.ProducteurLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.QteProdDTO;
import fr.amapj.service.services.meslivraisons.JourLivraisonsDTO.InfoPermanence;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.utilisateur.PreferenceUtilisateurEnum;
import fr.amapj.service.services.utilisateur.UtilisateurService;


public class AdherentNotificationService 
{
	private final static Logger logger = LogManager.getLogger();
	
	private Date getTodayDateWOTime() {
		return getDatePlusDayWOTime(0);
	}
	
	private Date getDatePlusDayWOTime( int nb ) {
		final Calendar day = Calendar.getInstance();
		day.add(Calendar.DAY_OF_YEAR, nb);
		day.set(Calendar.HOUR_OF_DAY, 0);
		day.set(Calendar.MINUTE, 0);
		day.set(Calendar.SECOND, 0);
		day.set(Calendar.MILLISECOND, 0);
		
		return day.getTime();
	}	
	
	@DbRead
	public void sendAdherentNotification(DeamonsContext deamonsContext)
	{	
		sendAdherentNotification(null,deamonsContext);
	}
	
	@DbRead
	public String sendAdherentNotification(String periode, DeamonsContext deamonsContext)
	{	
		ParametresDTO param = new ParametresService().getParametres();
		String defaultContenu = "Bonjour %PRENOM% %NOM%,<br/><br/>Vous trouverez ci-dessous les informations concernant la livraison du %DATE_LIVRAISON%.<br/><i><b>%PERMANENCE%<b></i><br/><br/>Ci-dessous les produits qui vous seront livrés :<br/>%PRODUITS_LIVRES%<br/><br/>Lieu de livraison : %LIEU_LIVRAISON%<br/>Heure de livraison : %HEURE_LIVRAISON%<br/><br/>Pour rappel le paniers qui ne seront pas récupérés avant la fin de la distribution seront distribués entre les amapiens de permanence pour cette distribution.<br/><br/>Vous pouvez consulter votre compte AMAPJ à tout moment en <a href=\"%LINK%\">cliquant ici</a>.<br/>Le comité<br/>%NOM_AMAP%";
		String defaultTitre = "%NOM_AMAP% : Livraison du %DATE_LIVRAISON%";
		Date dateJour = getTodayDateWOTime();
		logger.info("DATE JOUR : "+dateJour);
		logger.info("PERIODE : "+periode);
		EntityManager em = TransactionHelper.getEm();
		// On recherche la date de la prochaine livraison (>= date courante)
		Query qDate = em.createQuery("select min(mcd.dateLiv) from ModeleContratDate mcd" +
				" where mcd.dateLiv >= :dateRef and mcd.modeleContrat.etat=:etat");
		qDate.setParameter("etat", EtatModeleContrat.ACTIF);
		qDate.setParameter("dateRef", dateJour);
		
		// Récupérer la date de prochaine livraison
		List<Date> datesLivraison = qDate.getResultList();

		if( datesLivraison.size() > 0 && datesLivraison.get(0) != null ) {
			Date prochaineLivraison = datesLivraison.get(0);
			SimpleDateFormat df1 = new SimpleDateFormat("EEEEE dd MMMMM yyyy");

			String mode = null;
			Date nextDay = getDatePlusDayWOTime(1);
			if( periode != null && (periode.equals("JOUR") || periode.equals("VEILLE"))) {
				mode= periode;
			} else if( prochaineLivraison.equals(nextDay) ) {
				mode = "VEILLE";
			} else if (prochaineLivraison.equals(dateJour) ) {
				mode = "JOUR";
			} else {
				// Pas de notification à faire
				return "Aucune notification à envoyer (prochaine livraison le "+df1.format(prochaineLivraison)+").";
			}
			// Récupérer tous les utilisateurs qui doivent être notifiés pour cette livraison
			List<Utilisateur> users = getUserToNotify(em, prochaineLivraison, mode);
			
			MailerService ms = new MailerService();
			ModeleEmail modele = ms.getModeleEmail(ModeleEmailEnum.NOTIFICATION_PANIER);
			Dictionary dictionary = new Dictionary();
			int nbEnvoi = 0;
			String mailErreur=null;
			if( users != null ) {
				for (Utilisateur u : users) {
					dictionary.setUtilisateur(u, param);
					
					MesLivraisonsDTO res = new MesLivraisonsService().getMesLivraisons(prochaineLivraison,prochaineLivraison,null,u.getId());
					dictionary.addWord(DictionaryEnum.DATE_LIVRAISON,df1.format(prochaineLivraison));
					
					logger.info("nbJours : "+res.jours.size());
					// Pour chaque jour, ajout des informations permanence et produits livrés
					if( res.jours.size()!=0 ) {
						for (JourLivraisonsDTO jour : res.jours)
						{
	
							String perm = "";
							if (jour.permanences!=null && jour.permanences.size() > 0 )
							{
								// Il ne doit y en avoir qu'un
								InfoPermanence info = jour.permanences.get(0);
								perm =  "Vous êtes de permanence ce "+df1.format(prochaineLivraison)+" ("+
										info.periodePermanenceDTO.nom+")";
								
							}
							dictionary.addWord(DictionaryEnum.PERMANENCE, perm);
							StringBuffer contenu = new StringBuffer();
							logger.info("nbProducteurs : "+jour.producteurs.size());
	
							for (ProducteurLivraisonsDTO producteurLiv : jour.producteurs)
							{
								contenu.append("<br/>"+producteurLiv.producteur+" ("+producteurLiv.modeleContrat+")<br/>");
								contenu.append("<ul>");
								for (QteProdDTO cell : producteurLiv.produits)
								{
									logger.info("NbProduits"+producteurLiv.produits.size());
	
									contenu.append("<li>");
									contenu.append("("+cell.qte+"x) "+cell.nomProduit+" , "+cell.conditionnementProduit);
									contenu.append("</li>");
								}
								contenu.append("</ul>");
							}
							dictionary.addWord(DictionaryEnum.PRODUITS_LIVRES, contenu.toString());
						}
						String contenu = defaultContenu;
						String titre = defaultTitre;
						if(modele != null ) {
							contenu = modele.getContenu();
							titre = modele.getTitre();
						}
						String contenuMail = dictionary.substitue(contenu);					
						String titreMail = dictionary.substitue(titre);
	
						logger.info("Début de notification de l'utilisateur : "+u.getPrenom()+" "+u.getNom() );
						try
						{
							sendOneMessageNotificationUtilisateur(u, prochaineLivraison, titreMail, contenuMail, em, deamonsContext);
							nbEnvoi ++;
						}
						catch(Exception e)
						{
							mailErreur = (mailErreur!=null?";":"")+u.getEmail()+"(ou conjoint)";
							// En cas d'erreur, on intercepte l'exception pour permettre la notification des autres producteurs
							if( deamonsContext != null )
								deamonsContext.nbError++;
							logger.info("Erreur pour l'utilisateur "+u.getPrenom()+" "+u.getNom()+"\n"+StackUtils.asString(e));
						}
						logger.info("Fin de notification de l'utilisateur : "+u.getPrenom()+" "+u.getNom());
					}
				}	
			}
			return nbEnvoi+" message envoyés."+(mailErreur!=null?" Messages en erreur :"+mailErreur:" Aucun message en erreur");
		}
		return "Aucune notification à envoyer";
	}
	


//					sendOneMessageNotificationProducteur(modeleContratDate,dests,em,deamonsContext);

	
	/**
	 * Retourne la liste des utilisateurs de ce producteur à notifier et qui n'ont pas encore été notifié 
	 * pour cette date 
	 */
	private List<Utilisateur> getUserToNotify(EntityManager em, Date livraison, String mode)
	{
		logger.debug("On cherche les utilisateurs concernés par cette date de livraison : "+livraison);
		// On recherche tous les utilisateurs qui veulent être notifiés et qui n'ont pas encore été notifiés
		logger.info("Utilisateurs avec préférence : "+mode);
		Query q = em.createQuery(   "select distinct(u) from Utilisateur u, PreferenceUtilisateur pu, "+
				" Contrat c, ModeleContrat mc, ModeleContratDate mcd, NotificationDone nd WHERE " +
				" pu.utilisateur = u and c.utilisateur = u and c.modeleContrat = mc" +
				" and mcd.modeleContrat.etat=:etat and mcd.modeleContrat = mc " +
				" and mcd.dateLiv =:livraison "	+
				" and pu.nomPref=:nomPref and pu.valeurPref=:delai "+
				" and NOT EXISTS (select d from NotificationDone d where d.typNotificationDone=:typNotif and d.utilisateur=u and d.dateMailPeriodique=:livraison)");
		q.setParameter("livraison", livraison);
		q.setParameter("etat", EtatModeleContrat.ACTIF);
		q.setParameter("delai", mode);
		q.setParameter("nomPref", PreferenceUtilisateurEnum.PREF_DELAI_NOTIF_DISTRI.getNom());
		q.setParameter("typNotif", TypNotificationDone.MES_LIVRAISONS_ADHERENT);
		try {
			List<Utilisateur> us =  q.getResultList();
			logger.info("NB Util : "+us.size());
			return us;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void sendOneMessageNotificationUtilisateur(Utilisateur utilisateur, Date dateLivraison, String titre, String contenu, EntityManager em, DeamonsContext deamonsContext)
	{
		// Construction du message
		MailerMessage message  = new MailerMessage();

//		message.setTitle(param.nomAmap+" - Livraison du "+df.format(dateLivraison));
		message.setTitle(titre);
		message.setContent(contenu);
		message.setEmail(utilisateur.getEmail());
		try
		{
			sendMessageAndMemorize(message,dateLivraison,utilisateur.getId());
		}
		catch(Exception e)
		{
			// En cas d'erreur, on intercepte l'exception pour permettre la notification des autres destinatires
			if( deamonsContext != null)
				deamonsContext.nbError++;
			logger.error("Erreur pour notifier  "+utilisateur.getEmail()+"\n"+StackUtils.asString(e));
		}
		if( utilisateur.getEmailConjoint() != null && utilisateur.getEmailConjoint().trim().length() > 0) {
			if( sendToConjoint(utilisateur) ) {
				message.setEmail(utilisateur.getEmailConjoint());
				try
				{
					sendMessageAndMemorize(message,dateLivraison,utilisateur.getId());
				}
				catch(Exception e)
				{
					// En cas d'erreur, on intercepte l'exception pour permettre la notification des autres destinatires
					if(deamonsContext != null)
						deamonsContext.nbError++;
					logger.error("Erreur pour notifier  "+utilisateur.getEmailConjoint()+"\n"+StackUtils.asString(e));
				}
			}
		}
	}

	private void sendMail() {
		
	}
	private boolean sendToConjoint(Utilisateur u) {
		UtilisateurService us = new UtilisateurService();
		return us.conjointPreference(u);
	}
	
	/**
	 * On réalise chaque envoi dans une transaction indépendante 
	 * 
	 * @param email
	 * @param title
	 * @param content
	 * @param generator
	 */
	private void sendMessageAndMemorize(final MailerMessage message, final Date dateLivraison, final Long utilisateurId)
	{
		NewTransaction.write(new Call()
		{
			@Override
			public Object executeInNewTransaction(EntityManager em)
			{
				sendMessageAndMemorize(em, dateLivraison, message, utilisateurId);
				return null;
			}
		});
		
	}

	protected void sendMessageAndMemorize(EntityManager em, Date dateLivraison, MailerMessage message, Long utilisateurId)
	{
		// On mémorise dans la base de données que l'on va envoyer le message
		NotificationDone notificationDone = new NotificationDone();
		notificationDone.typNotificationDone = TypNotificationDone.MES_LIVRAISONS_ADHERENT;
		notificationDone.modeleContratDate = null;
		notificationDone.utilisateur = em.find(Utilisateur.class, utilisateurId);
		notificationDone.dateMailPeriodique = dateLivraison;
		notificationDone.dateEnvoi = DateUtils.getDate();
		em.persist(notificationDone);
		
		// On envoie le message
		new MailerService().sendHtmlMail(message);
	}

}
