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
 package fr.amapj.view.views.droits;

import java.util.List;

import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.access.AdminTresorierDTO;
import fr.amapj.view.engine.listpart.ButtonType;
import fr.amapj.view.engine.listpart.StandardListPart;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;


/**
 * Gestion des administrateurs
 *
 */
@SuppressWarnings("serial")
public class DroitsAdministrateurListPart extends StandardListPart<AdminTresorierDTO> 
{

	public DroitsAdministrateurListPart()
	{
		super(AdminTresorierDTO.class,false);
	}
	
	
	@Override
	protected String getTitle() 
	{
		return "Liste des administrateurs";
	}


	@Override
	protected void drawButton() 
	{
		addButton("Ajouter un administrateur",ButtonType.ALWAYS,()->handleAjouter());
		addButton("Supprimer un administrateur",ButtonType.EDIT_MODE,()->handleSupprimer());
		
		addSearchField("Rechercher par nom ou prenom");
	}


	@Override
	protected void drawTable() 
	{
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "nom", "prenom"  });
		cdesTable.setColumnHeader("nom","Nom");
		cdesTable.setColumnHeader("prenom","Prenom");
		
	}



	@Override
	protected List<AdminTresorierDTO> getLines() 
	{
		return new AccessManagementService().getAllAdmin();
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
	

	private void handleAjouter()
	{
		AdminTresorierEditorPart.open(new AdminTresorierEditorPart(true), this);
	}


	private void handleSupprimer()
	{
		AdminTresorierDTO dto = getSelectedLine();
		String text = "Etes vous sûr de vouloir supprimer le droit administrateur à "+dto.nom+" "+dto.prenom+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,dto.id,e->new AccessManagementService().deleteAdmin(e));
		confirmPopup.open(this);		
	}
}
