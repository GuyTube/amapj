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
 package fr.amapj.view.views.utilisateur;

import java.util.List;

import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.service.services.edgenerator.excel.EGListeAdherent;
import fr.amapj.service.services.edgenerator.excel.EGListeAdherent.Type;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.view.engine.excelgenerator.LinkCreator;
import fr.amapj.view.engine.listpart.ButtonType;
import fr.amapj.view.engine.listpart.StandardListPart;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.tools.DateTimeToStringConverter;
import fr.amapj.view.views.common.amapientelecharger.TelechargerAmapien;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.views.compte.PopupSaisiePassword;


/**
 * Gestion des utilisateurs
 *
 */
@SuppressWarnings("serial")
public class UtilisateurListPart extends StandardListPart<UtilisateurDTO>
{

	public UtilisateurListPart()
	{
		super(UtilisateurDTO.class,false);
	}
	
	
	@Override
	protected String getTitle() 
	{
		return "Liste des utilisateurs";
	}


	@Override
	protected void drawButton() 
	{
		addButton("Créer un nouvel utilisateur", ButtonType.ALWAYS, ()->handleAjouter());
		addButton("Modifier", ButtonType.EDIT_MODE, ()->handleEditer());
		addButton("Voir", ButtonType.EDIT_MODE, ()->handleVoir());
		addButton("Changer le mot de passe", ButtonType.EDIT_MODE, ()->handleChangerPassword());
		addButton("Archiver", ButtonType.EDIT_MODE, ()->handleChangeState());
		addButton("Télécharger", ButtonType.EDIT_MODE, ()->handleTelecharger());
		addButton("Autre...", ButtonType.ALWAYS, ()->handleMore());

		addSearchField("Rechercher par nom ou prénom");
	}

	@Override
	protected void addExtraComponent() 
	{
		addComponent(LinkCreator.createLink(new EGListeAdherent(Type.STD)));
		
	}

	@Override
	protected void drawTable() 
	{
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "nom", "prenom" ,"roles","etatUtilisateur" ,"dateCreation"});
		cdesTable.setColumnHeader("nom","Nom");
		cdesTable.setColumnHeader("prenom","Prenom");
		cdesTable.setColumnHeader("roles","Role");
		cdesTable.setColumnHeader("etatUtilisateur","Etat");
		cdesTable.setColumnHeader("dateCreation","Date création");
		
		cdesTable.setConverter("dateCreation", new DateTimeToStringConverter());
	}



	@Override
	protected List<UtilisateurDTO> getLines() 
	{
		return new UtilisateurService().getAllUtilisateurs(EtatUtilisateur.ACTIF);
	}


	@Override
	protected String[] getSortInfos() 
	{
		return new String[] { "nom" , "prenom" };
	}
	
	protected String[] getSearchInfos()
	{
		return new String[] { "nom" , "prenom" };
	}
	
	
	private void handleChangerPassword()
	{
		UtilisateurDTO dto = getSelectedLine();
		FormPopup.open(new PopupSaisiePassword(dto.id),this);		
	}
	


	private void handleChangeState()
	{
		UtilisateurDTO dto = getSelectedLine();
		FormPopup.open(new PopupUtilisateurArchiver(dto),this);
	}
	

	private void handleAjouter()
	{
		CreationUtilisateurEditorPart.open(new CreationUtilisateurEditorPart(), this);
	}
	
	private void handleMore()
	{
		UtilisateurDTO dto = getSelectedLine();
		ChoixActionUtilisateur.open(new ChoixActionUtilisateur(dto), this);
	}


	protected void handleEditer()
	{
		UtilisateurDTO dto = getSelectedLine();
		ModificationUtilisateurEditorPart.open(new ModificationUtilisateurEditorPart(dto.id), this);
	}
	
	protected void handleTelecharger()
	{
		UtilisateurDTO dto = getSelectedLine();
		TelechargerAmapien.handleTelecharger(dto.id, this);
	}
	
	
	protected void handleVoir()
	{
		UtilisateurDTO dto = getSelectedLine();
		PopupUtilisateurVoirPart.open(new PopupUtilisateurVoirPart(dto.id), this);
	}

}
