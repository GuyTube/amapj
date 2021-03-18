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
 package fr.amapj.view.views.producteur.basicform;

import java.util.List;

import com.vaadin.ui.Table.Align;

import fr.amapj.model.models.fichierbase.EtatProducteur;
import fr.amapj.service.services.producteur.ProducteurDTO;
import fr.amapj.service.services.producteur.ProducteurService;
import fr.amapj.view.engine.listpart.ButtonType;
import fr.amapj.view.engine.listpart.StandardListPart;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.tools.DateToStringConverter;


/**
 * Gestion des producteurs
 *
 */
@SuppressWarnings("serial")
public class ProducteurListPart extends StandardListPart<ProducteurDTO>
{

	public ProducteurListPart()
	{
		super(ProducteurDTO.class,false);
	}
	
	
	@Override
	protected String getTitle() 
	{
		return "Liste des producteurs";
	}


	@Override
	protected void drawButton() 
	{
		addButton("Créer un nouveau producteur",ButtonType.ALWAYS,()->handleAjouter());
		addButton("Modifier",ButtonType.EDIT_MODE,()->handleEditer());
		addButton("Voir",ButtonType.EDIT_MODE,()->handleVoir());
		addButton("Supprimer",ButtonType.EDIT_MODE,()->handleSupprimer());
		addButton("Archiver",ButtonType.EDIT_MODE,()->handleArchiver());
		
		addSearchField("Rechercher par nom");
	}


	@Override
	protected void drawTable() 
	{
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "nom", "utilisateurInfo" ,"referentInfo" , "nbModeleContratActif", "dateDerniereLivraison", "dateCreation"});
		
		cdesTable.setColumnHeader("nom","Nom");
		cdesTable.setColumnHeader("utilisateurInfo","Producteurs");
		cdesTable.setColumnHeader("referentInfo","Referents");
		
		cdesTable.setColumnHeader("nbModeleContratActif","Nb contrats");
		cdesTable.setColumnHeader("dateDerniereLivraison","Dernière liv");
		
		cdesTable.setColumnHeader("dateCreation","Date création");
		
		cdesTable.setConverter("dateDerniereLivraison", new DateToStringConverter());
		cdesTable.setConverter("dateCreation", new DateToStringConverter());
		
		cdesTable.setColumnAlignment("nbModeleContratActif",Align.CENTER);
		
	}



	@Override
	protected List<ProducteurDTO> getLines() 
	{
		return new ProducteurService().getAllProducteurs(EtatProducteur.ACTIF);
	}


	@Override
	protected String[] getSortInfos() 
	{
		return new String[] { "nom" };
	}
	
	protected String[] getSearchInfos()
	{
		return new String[] { "nom" };
	}
	

	private void handleAjouter()
	{
		ProducteurEditorPart.open(new ProducteurEditorPart(true,null), this);
	}


	private void handleEditer()
	{
		ProducteurDTO dto = getSelectedLine();
		ProducteurEditorPart.open(new ProducteurEditorPart(false,dto.id), this);
	}
	
	private void handleVoir()
	{
		ProducteurDTO dto = getSelectedLine();
		ProducteurVoirPart.open(new ProducteurVoirPart(dto), this);
	}
	

	private void handleSupprimer()
	{
		ProducteurDTO dto = getSelectedLine();
		String text = "Etes vous sûr de vouloir supprimer le producteur "+dto.nom+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,dto.id,e->new ProducteurService().delete(e));
		confirmPopup.open(this);		
	}
	

	
	
	private void handleArchiver()
	{
		ProducteurDTO dto = getSelectedLine();
		PopupProducteurArchiver.open(new PopupProducteurArchiver(dto), this);
	}
	
}
