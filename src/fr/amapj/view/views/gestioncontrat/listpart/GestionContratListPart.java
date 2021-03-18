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
 package fr.amapj.view.views.gestioncontrat.listpart;

import java.util.List;

import com.vaadin.ui.Table.Align;

import fr.amapj.model.engine.IdentifiableUtil;
import fr.amapj.model.models.acces.RoleList;
import fr.amapj.model.models.contrat.modele.EtatModeleContrat;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.listpart.ButtonType;
import fr.amapj.view.engine.listpart.StandardListPart;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.swicthpopup.DirectSwitchPopup;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.views.common.contrattelecharger.TelechargerContrat;
import fr.amapj.view.views.gestioncontrat.editorpart.ChoixModifEditorPart;
import fr.amapj.view.views.gestioncontrat.editorpart.GestionContratEditorPart;
import fr.amapj.view.views.gestioncontrat.editorpart.ModeleContratVisualiserPart;
import fr.amapj.view.views.saisiecontrat.SaisieContrat;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.ModeSaisie;


/**
 * Gestion des modeles de contrats : création, diffusion, ...
 *
 */
public class GestionContratListPart extends StandardListPart<ModeleContratSummaryDTO>
{	
	private List<Producteur> allowedProducteurs;	
	
	
	public GestionContratListPart()
	{
		super(ModeleContratSummaryDTO.class,false);
		allowedProducteurs = new AccessManagementService().getAccessLivraisonProducteur(SessionManager.getUserRoles(),SessionManager.getUserId(),true);
	}
	
	
	@Override
	protected String getTitle() 
	{
		return "Liste des contrats vierges" ;
	}


	@Override
	protected void drawButton() 
	{
		addButton("Créer",ButtonType.ALWAYS,e-> new GestionContratEditorPart(null,allowedProducteurs));
		addButton("Créer à partir de ...", ButtonType.EDIT_MODE,e->new GestionContratEditorPart(e.id,allowedProducteurs));
		addButton("Voir",ButtonType.EDIT_MODE,e->new ModeleContratVisualiserPart(e.id));
		addButton("Modifier",ButtonType.EDIT_MODE,e->new ChoixModifEditorPart(e.id));
		addButton("Tester",ButtonType.EDIT_MODE, ()->handleTester());
		addButton("Télécharger ...",ButtonType.EDIT_MODE,()->handleTelecharger());
		addButton("Changer l'état",ButtonType.EDIT_MODE, e->new PopupSaisieEtat(e));
		addButton("Archiver",ButtonType.EDIT_MODE, e->new PopupContratArchiver(e));
		addButton("Autre",ButtonType.ALWAYS, ()->handleAutre());
			
		addSearchField("Rechercher par nom ou producteur");		
	}

	@Override
	protected void drawTable() 
	{
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "etat", "nom", "nomProducteur" ,"finInscription","dateDebut" , "dateFin" , "nbLivraison" ,"nbInscrits"});
		cdesTable.setColumnHeader("etat","Etat");
		cdesTable.setColumnHeader("nom","Nom");
		cdesTable.setColumnHeader("nomProducteur","Producteur");
		cdesTable.setColumnHeader("finInscription","Fin inscription");
		cdesTable.setColumnHeader("dateDebut","Première livraison");
		cdesTable.setColumnHeader("dateFin","Dernière livraison");
		cdesTable.setColumnHeader("nbLivraison","Livraisons");
		cdesTable.setColumnHeader("nbInscrits","Contrats signés");
		
		
		//
		cdesTable.setConverter("finInscription", new DateToStringConverter());
		cdesTable.setConverter("dateDebut", new DateToStringConverter());
		cdesTable.setConverter("dateFin", new DateToStringConverter());
		
		cdesTable.setColumnAlignment("nbLivraison",Align.CENTER);
		cdesTable.setColumnAlignment("nbInscrits",Align.CENTER);
	}



	@Override
	protected List<ModeleContratSummaryDTO> getLines() 
	{
		return new GestionContratService().getModeleContratInfo(EtatModeleContrat.CREATION,EtatModeleContrat.ACTIF);
	}


	@Override
	protected String[] getSortInfos() 
	{
		return new String[] {  "etat" , "nomProducteur" , "dateDebut" };
	}
	
	@Override
	protected String[] getSearchInfos()
	{
		return new String[] { "nom" , "nomProducteur" };
	}
	
	
	private void handleTelecharger()
	{
		ModeleContratSummaryDTO mcDto = getSelectedLine();
		TelechargerContrat.displayPopupTelechargerContrat(mcDto.id, null, this);
	}
	
	private void handleTester()
	{
		ModeleContratSummaryDTO mcDto = getSelectedLine();
		SaisieContrat.saisieContrat(mcDto.id,null,null,"Mode Test",ModeSaisie.FOR_TEST,this);	
	}
	
	private void handleAutre()
	{
		ModeleContratSummaryDTO dto =  getSelectedLine();
		
		DirectSwitchPopup popup = new DirectSwitchPopup("Autres actions possibles sur les modeles de contrats",60);
			
		popup.setLine1("Veuillez indiquer ce que vous souhaitez faire :");

		if (isEditAllowed())
		{
			popup.addLine("Supprimer ce modéle de contrat", handleSupprimer(dto));
		}
		
		List<RoleList> roles = SessionManager.getUserRoles();
		if ( (roles.contains(RoleList.ADMIN)) ||  (roles.contains(RoleList.TRESORIER)) )
		{
			popup.addLine("Saisir en masse les périodes de cotisations pour tous les modéles de contrats", new SaisieEnMassePeriodeCotisation());
		}
			
		popup.open(this);
	}
	
	
	private SuppressionPopup handleSupprimer(ModeleContratSummaryDTO mcDTO)
	{
		String text = "Etes vous sûr de vouloir supprimer le contrat vierge "+mcDTO.nom+" de "+mcDTO.nomProducteur+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,mcDTO.id,e->new GestionContratService().deleteContrat(e));
		return confirmPopup;	
	}


	/**
	 * Retourne true si l'utilisateur courant a le droit de manipuler ce contrat
	 * @return
	 */
	@Override
	protected boolean isEditAllowed()
	{
		ModeleContratSummaryDTO mcDto = getSelectedLine();
		if (mcDto==null)
		{
			return false;
		}
		
		return IdentifiableUtil.contains(allowedProducteurs, mcDto.producteurId);
	}
}
