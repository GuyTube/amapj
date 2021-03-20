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
 package fr.amapj.view.views.sendmail;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import fr.amapj.service.services.edgenerator.excel.feuilledistribution.producteur.EGFeuilleDistributionProducteur;
import fr.amapj.service.services.edgenerator.excel.feuilledistribution.producteur.EGSyntheseContrat;
import fr.amapj.service.services.edgenerator.excel.producteur.EGPaiementProducteur;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.service.services.mailer.HistoriqueEmailDTO;
import fr.amapj.service.services.mailer.MailerService;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.service.services.producteur.ProducteurDTO;
import fr.amapj.service.services.producteur.ProducteurService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.service.services.session.SessionParameters;
import fr.amapj.service.services.suiviacces.ConnectedUserDTO;
import fr.amapj.view.engine.excelgenerator.TelechargerPopup;
import fr.amapj.view.engine.listpart.ButtonType;
import fr.amapj.view.engine.listpart.StandardListPart;
import fr.amapj.view.engine.notification.NotificationHelper;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.corepopup.CorePopup.ColorStyle;
import fr.amapj.view.engine.popup.errorpopup.ErrorPopup;
import fr.amapj.view.engine.popup.messagepopup.MessagePopup;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.views.producteur.ProducteurSelectorPart;
import fr.amapj.view.views.saisiecontrat.SaisieContrat;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.ModeSaisie;
import fr.amapj.view.views.utilisateur.ChoixActionUtilisateur;
import fr.amapj.view.views.utilisateur.PopupEmailErreur;
import fr.amapj.view.views.utilisateur.PopupEnvoiEmail;


/**
 * Liste des messages envoyés
 *
 */
public class ListeMessagesEnvoyesView extends StandardListPart<HistoriqueEmailDTO> implements PopupSuppressionListener
{
	
	public ListeMessagesEnvoyesView()
	{
		super(HistoriqueEmailDTO.class,false);
	}
	
	
	@Override
	protected String getTitle() 
	{
		return "Liste des messages envoyés";
	}
	

	@Override
	protected void drawButton() 
	{
		addButton("Nouveau message",ButtonType.ALWAYS,()->handleNouveauMessage());
		addButton("Utiliser comme modèle",ButtonType.EDIT_MODE,()->handleRenvoyer());
		addButton("Effacer",ButtonType.EDIT_MODE,()->handleEffacerMessage());
		addButton("Voir mails en erreur",ButtonType.EDIT_MODE,()->handleAfficherEmailsErreur());
	}
	
	@Override
	protected void drawTable() 
	{
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "dateEnvoi", "sujet", "mixExpediteur","role","hasEmailErreur"});
		cdesTable.setColumnHeader("dateEnvoi","Date d'envoi");
		cdesTable.setColumnHeader("sujet","Sujet");
		cdesTable.setColumnHeader("mixExpediteur","Expéditeur");
		cdesTable.setColumnHeader("role","Rôle de l'expéditeur");
		cdesTable.setColumnHeader("hasEmailErreur","Err");
		//
		cdesTable.setConverter("dateEnvoi", new DateToStringConverter());
		cdesTable.setColumnWidth("dateEnvoi",120);
		cdesTable.setColumnWidth("sujet",-1);
		cdesTable.setColumnWidth("mixExpediteur",220);
		cdesTable.setColumnWidth("role",250);
		cdesTable.setColumnWidth("hasEmailErreur",60);
	
}



	@Override
	protected List<HistoriqueEmailDTO> getLines() 
	{
		SessionParameters p = SessionManager.getSessionParameters();
		ProducteurService ps = new ProducteurService();
		ProducteurDTO pDto = null;
		if (p.producteurId != null ) {
			pDto = ps.loadProducteur(p.producteurId);
		}
		List<HistoriqueEmailDTO> res = new MailerService().getHistoriqueMail(p.userRole, pDto);
		return res;
	}


	@Override
	protected String[] getSortInfos() 
	{
		return new String[] { "dateEnvoi" , "roleExpediteur"  };
	}
	
	protected boolean[] getSortAsc()
	{
		return new boolean[] { false , false  };
	}
	
	
		

	private void handleRenvoyer()
	{
		HistoriqueEmailDTO hmDto = getSelectedLine();
		// Ouverture popup pour renvoi d'un message existant
		PopupEnvoiEmail.open(new PopupEnvoiEmail(hmDto), this);
	}

	private void handleEffacerMessage() {
		HistoriqueEmailDTO hmDto = getSelectedLine();
		
		String text = "Etes vous sûr de vouloir supprimer ce message";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,hmDto.getId());
		SuppressionPopup.open(confirmPopup, this);
	}

	private void handleNouveauMessage()
	{
		// Ouverture popup pour envoi d'un nouveau message
		PopupEnvoiEmail.open(new PopupEnvoiEmail(), this);
	}
	
	private void handleAfficherEmailsErreur()
	{
		HistoriqueEmailDTO hmDto = getSelectedLine();
		if( hmDto.getHasEmailErreur() != null && hmDto.getHasEmailErreur().equals("X")) {
			// Ouverture popup pour afficher les mails en erreur
			PopupEmailErreur.open(new PopupEmailErreur(hmDto), this);	
		} else {
			NotificationHelper.displayInfo("Aucune erreur lors de l'envoi");
		}
		
	}
	
	@Override
	protected String[] getSearchInfos() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException {
		// Suppression d'un message 
		MailerService.deleteEmail(idItemToSuppress);
	}
}
