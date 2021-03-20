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
 package fr.amapj.view.views.utilisateur;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;

import fr.amapj.model.models.acces.RoleList;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.fichierbase.ExpediteurAmap;
import fr.amapj.model.models.fichierbase.HistoriqueEmail;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.model.models.param.ChoixOuiNon;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.service.services.gestioncontratsigne.GestionContratSigneService;
import fr.amapj.service.services.mailer.HistoriqueEmailDTO;
import fr.amapj.service.services.mailer.HistoriqueEmailService;
import fr.amapj.service.services.mailer.MailerMessage;
import fr.amapj.service.services.mailer.MailerService;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.producteur.ProducteurDTO;
import fr.amapj.service.services.producteur.ProducteurService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.service.services.session.SessionParameters;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.service.services.utilisateur.envoimail.EnvoiMailDTO;
import fr.amapj.service.services.utilisateur.envoimail.EnvoiMailUtilisateurDTO;
import fr.amapj.service.services.utilisateur.envoimail.StatusEnvoiMailDTO;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.enumselector.EnumSearcher;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.formpopup.AbstractFormPopup;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.popup.formpopup.validator.IValidator;
import fr.amapj.view.engine.popup.formpopup.validator.NotNullValidator;
import fr.amapj.view.engine.popup.formpopup.validator.StringLengthValidator;
import fr.amapj.view.engine.popup.formpopup.validator.UniqueInDatabaseValidator;
import fr.amapj.view.engine.popup.messagepopup.MessagePopup;
import fr.amapj.view.views.searcher.SearcherList;
import fr.amapj.view.views.sendmail.ExpediteurAmapSelectorPart;
import fr.amapj.view.views.sendmail.SouscripteursSelectorPart;

/**
 * Popup 
 * 
 *
 */
public class PopupEnvoiEmail extends CorePopup
{

	TextField titre;
	TextField listeDestinatairesField;
	RichTextArea zoneTexte;
	SouscripteursSelectorPart souscripteurCombo;
	ExpediteurAmapSelectorPart expediteurCombo;
	HistoriqueEmailDTO modeleEmail;
	Button envoyer;
	Button annuler;
	CheckBox checkboxMonAdresse;
	VerticalLayout layout;
	CheckBox cbConjoints;
	boolean resultatRecherche = false;
	List<Utilisateur> destinataires;

	private final static Logger logger = LogManager.getLogger();

	/**
	 * 
	 */
	public PopupEnvoiEmail()
	{
		init(null);
	}
	

	public PopupEnvoiEmail(HistoriqueEmailDTO hm) 
	{
		init(hm);
	}
	
	public PopupEnvoiEmail(List<Utilisateur> users)
	{
		init(null);
		resultatRecherche = true;
		destinataires = users;
		
	}
	
	private void init(HistoriqueEmailDTO hm) {
		setWidth(60);
		popupTitle = "Envoi d'emails";
		modeleEmail = hm;
		if( modeleEmail == null ) {
			modeleEmail = new HistoriqueEmailDTO();
			modeleEmail.setSujet("");
			modeleEmail.setContenu(""); 
		}
	}
	
	/**
	 * 
	 */
	@Override
	protected void createContent(VerticalLayout contentLayout) {
		
		ParametresDTO param = new ParametresService().getParametres();
		SessionParameters p = SessionManager.getSessionParameters();

		layout = contentLayout;
				
		/*layout.addStyleName("v-scrollable");
		layout.setHeightUndefined();*/
		addLabel(layout, "Cet outil permet d'envoyer un mail à une personne, ou à un groupe de personnes.");
		addLabel(layout, "Veuillez utiliser cette fonctionnalité avec parcimonie.");
		addEmptyLine(layout);

		// Combo permettant de selection un expéditeur
		expediteurCombo = new ExpediteurAmapSelectorPart();
		HorizontalLayout tbExpediteur = expediteurCombo.getChoixExpediteurAmapComponent();
		layout.addComponent(tbExpediteur);
		// Les producteurs peuvent utiliser leur adresse mail de contact
		if( p.producteurId != null ) {
			ProducteurService ps = new ProducteurService();
			ProducteurDTO pDto = ps.loadProducteur(p.producteurId);
			checkboxMonAdresse = new CheckBox();
			String adresseContact = "(non définie)";
			checkboxMonAdresse.setReadOnly(true);
			if( pDto!=null && pDto.getEmailContact() != null ) {
				adresseContact = "("+pDto.getEmailContact()+")";
				checkboxMonAdresse.setReadOnly(false);
			}
			checkboxMonAdresse.setCaption("Utiliser mon adresse de contact "+adresseContact);
			
			checkboxMonAdresse.addValueChangeListener(event -> {
				if( checkboxMonAdresse.getValue() == true ) {
					expediteurCombo.disable();
				} else {
					expediteurCombo.enable();
				}
			});
			layout.addComponent(checkboxMonAdresse);
		}
		//layout.addComponent(toolbar2);

		// Liste des destinataires
		if(resultatRecherche) { // Cas de destinataire choisi par une recherche dans un autre écran
			listeDestinatairesField = addTextField(layout, "Destinataires");
			String listeDest="";
			// Liste des destinataires (non modifiable)
			for(Utilisateur u:destinataires) {
				listeDest=listeDest+u.getNom()+" "+u.getPrenom()+";";
			}
			listeDestinatairesField.setValue(listeDest);
			listeDestinatairesField.setEnabled(false);
//			tbDestinataires = new HorizontalLayout();
//			tbDestinataires.addComponent(listeDestinatairesField);
		} else { // Liste de destinataires standards fonction des contrats existants
			HorizontalLayout tbDestinataires;
			souscripteurCombo = new SouscripteursSelectorPart();		
			tbDestinataires = souscripteurCombo.getChoixContratComponent();
			layout.addComponent(tbDestinataires);
		}

		// Choix d'ajouter les conjoints ou non aux destinataires
		HorizontalLayout tbConjoint = new HorizontalLayout();
		tbConjoint.setSpacing(true);


		cbConjoints = addCheckBox(layout, "Inclure Conjoints ?");
		
		layout.addComponent(tbConjoint);

		// TextField pour saisir le sujet du mail
		titre = addTextField(layout, "Titre du mail");
		zoneTexte = new RichTextArea("Message");
		zoneTexte.setWidth("100%");
		layout.addComponent(zoneTexte);
		
		// Mise en place des valeurs par défaut
		if( modeleEmail != null ) {
			titre.setValue( modeleEmail.getSujet() );
			if( !resultatRecherche )
				expediteurCombo.setExpediteurAmapId(modeleEmail.getExpediteurId());
			zoneTexte.setValue(modeleEmail.getContenu());
		}

		
	}

	private CheckBox addCheckBox(VerticalLayout layout, String lib)
	{
		HorizontalLayout h = new HorizontalLayout();
		h.setSpacing(true);
		
		CheckBox cb = new CheckBox("");
		cb.setWidth("400px");
		
		Label l = new Label(lib);
		l.setWidth("150px");
		h.addComponent(l);
		h.addComponent(cb);
		layout.addComponent(h);
		return cb;
		
	}
	private TextField addTextField(VerticalLayout layout, String lib)
	{
		HorizontalLayout h = new HorizontalLayout();
		h.setSpacing(true);
		//h.setWidth("100%");
		
		TextField tf = new TextField();
		tf.setWidth("500px");
		
		Label l = new Label(lib);
		l.setWidth("150px");
		h.addComponent(l);
		h.addComponent(tf);
		layout.addComponent(h);
		return tf;
		
	}

	private void alerte(String titre, String message) {
		ArrayList<String> messages = new ArrayList<String>();
		if(titre == null)
			titre = "Saisie incorrecte";
		messages.add(message);
		MessagePopup.open(new MessagePopup(titre, messages));
	}
	
	private void handleEnvoyerMail()
	{
		if( titre.getValue().trim().length() == 0 ) {
			alerte(null,"Veuillez saisir le titre du message");
			envoyer.setEnabled(true);
			return;
		}

		if( expediteurCombo.getExpediteurAmapId() == null && (checkboxMonAdresse == null || checkboxMonAdresse.getValue() == false) ) {
			alerte(null,"Veuillez choisir l'expéditeur");
			envoyer.setEnabled(true);
			return;
		}

		if( !resultatRecherche && souscripteurCombo.getModeleContratId() == null ) {
			alerte(null,"Veuillez choisir les destinataires");
			envoyer.setEnabled(true);
			return;
		}
		
		if( zoneTexte.getValue() == null || zoneTexte.getValue().trim().length() == 0 ) {
			alerte(null,"Veuillez saisir le texte du message");
			envoyer.setEnabled(true);
			return;
		}
		
		SessionParameters p = SessionManager.getSessionParameters();

		ExpediteurAmap ea = MailerService.getExpediteurAmap(expediteurCombo.getExpediteurAmapId());
		String adresseExpediteur = null;
		if( ea == null && checkboxMonAdresse.getValue()) {
			adresseExpediteur = getAdresseContactProducteur(p.userId);
		}
		Set<String> allMails = new HashSet<String>();
		if( resultatRecherche ) {
			logger.info("RESULTATS RECHERCHES");
			for(Utilisateur u : destinataires) {
				allMails.add(u.getEmail());
				if(u.getEmailConjoint()!= null && cbConjoints.getValue()) {
					allMails.add(u.getEmailConjoint());
				}
			}
		} else {
			logger.info("PAR CONTRAT");

			GestionContratSigneService serviceContrats = new GestionContratSigneService();
			Long contratId = souscripteurCombo.getModeleContratId();
			if(contratId.equals(SouscripteursSelectorPart.SpecialValue.TESTMAIL.id())) {
				logger.info("TESTMAIL");

				allMails.add(p.userEmail);
				if( cbConjoints.getValue() ) {
					allMails.add(p.emailConjoint);
				}
			} else if(contratId.equals(SouscripteursSelectorPart.SpecialValue.ALL_CONTRATS.id())) {
				logger.info("ALL CONTRATS");

				List<ModeleContrat> mcs = new GestionContratSigneService().getModeleContratActif();
				for (ModeleContrat mc : mcs)
				{
					allMails.addAll(serviceContrats.getAllMails(mc.getId(),cbConjoints.getValue() ));
				}
			} else {
				logger.info("PAR CONTRAT "+contratId);

				allMails.addAll(serviceContrats.getAllMails(contratId, cbConjoints.getValue()));
			}
		}
		logger.info("ALL MAIL : "+allMails.size());

		//Notification.show("Veuillez patienter pendant l'envoi des messages");
		String errorMailStr = "";
		int errorMail = 0;
		int successMail = 0;
		// Envoi des mails un par un à chaque destinataire (meilleure solution ?)
		
		for( String mail : allMails ) {
			String link = new ParametresService().getParametres().getUrl()+"?username="+mail;
			String subject = titre.getValue();
			String htmlContent = zoneTexte.getValue();
			htmlContent = htmlContent.replaceAll("#LINK#", link);	
			try {
				new MailerService().sendHtmlMail( new MailerMessage(mail, subject, htmlContent, ea.getEmail(), ea.getEmailRetour() ));
				successMail++;
				logger.info("Mail envoyé à "+mail); 
			} catch(Exception e) {
				errorMail++;
				errorMailStr = errorMailStr+";"+mail;
			}
		}
		Notification.show(allMails.size() +" message(s) envoyé(s)");
		saveMail(titre.getValue(),adresseExpediteur,p.userRole.get(0),p.userNom+" "+p.userPrenom, ea, zoneTexte.getValue(),errorMailStr);
		close();
	}

	private void saveMail(String sujet, String adresseExpediteur, RoleList role, String userName, ExpediteurAmap expediteurAmap, String contenu, String errorMails) {
		HistoriqueEmail message=new HistoriqueEmail();
		message.setContenu(contenu);
		Timestamp datetime = new Timestamp(new Date().getTime());
		message.setDateHeureEnvoi(datetime);
		message.setExpediteurAmap(expediteurAmap);
		message.setRole(role.name());
		message.setSujet(sujet);
		message.setUserExpediteur(userName);
		message.setErrorMails(errorMails);
		message.setAdresseExpediteur(adresseExpediteur);

		MailerService.saveMail(message);
	}
	
	private Button addButton(Layout layout, String str,ClickListener listener)
	{
		Button b = new Button(str);
		b.addStyleName("primary");
		b.addClickListener(listener);
		layout.addComponent(b);
		return b;
	}
	
	
	private Label addLabel(VerticalLayout layout, String str)
	{
		Label tf = new Label(str);
		layout.addComponent(tf);
		return tf;

	}
	
	private Label addEmptyLine(VerticalLayout layout)
	{
		Label tf = new Label("<br/>",ContentMode.HTML);
		layout.addComponent(tf);
		return tf;

	}

	
	protected List<ModeleContratSummaryDTO> getListeContrats() 
	{
		return new ProducteurService().getModeleContratInfoAll();
	}
	
	@Override
	protected void createButtonBar() {
		envoyer = addButton("Envoyer",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleEnvoyerMail();
			}
		});
		annuler = addButton("Annuler",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				close();
			}
		});
		// Pour éviter que le message soit envoyé à nouveau
		envoyer.setDisableOnClick(true);
		
	}
	
	private String getAdresseContactProducteur(Long userId) {
		Producteur p = HistoriqueEmailService.getProducteur(userId);
		return p.getEmailContact();
	}
}
