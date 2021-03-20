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
 package fr.amapj.view.views.carteprepayee;

import java.util.List;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Table.Align;

import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.service.services.carteprepayee.CartePrepayeeItemDTO;
import fr.amapj.service.services.carteprepayee.GestionCartePrepayeeService;
import fr.amapj.service.services.producteur.ProducteurService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.listpart.ButtonType;
import fr.amapj.view.engine.listpart.StandardListPart;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.corepopup.CorePopup.ColorStyle;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.popup.messagepopup.MessagePopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.tools.DateTimeToStringConverter;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;
import fr.amapj.view.views.common.utilisateurselector.UtilisateurSelectorPart;
import fr.amapj.view.views.producteur.ProducteurSelectorPart;
import fr.amapj.view.views.saisiecontrat.SaisieContrat;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.ModeSaisie;


/**
 * Affichage des contrats pour un producteur 
 *
 */
public class CartesPrepayeesAdminView extends StandardListPart<CartePrepayeeItemDTO>
{
	private ProducteurSelectorPart producteurSelector;
	
	private UtilisateurSelectorPart utilisateurSelector;

	public CartesPrepayeesAdminView()
	{
		super(CartePrepayeeItemDTO.class,false);
		
	}
	
	
	@Override
	protected String getTitle() 
	{
		return "Gestion des cartes prépayées";
	}
	
	@Override
	protected void addSelectorComponent()
	{
		producteurSelector = new ProducteurSelectorPart(this);
		addComponent(producteurSelector.getChoixProducteurComponent());

		utilisateurSelector = new UtilisateurSelectorPart(this);
		addComponent(utilisateurSelector.getChoixUtilisateurComponent());

	}


	@Override
	protected void drawButton() 
	{
		addButton("Créer",ButtonType.ALWAYS,()->handleCreer());
		addButton("Supprimer",ButtonType.EDIT_MODE,()->handleSupprimer());
		addButton("Basculer paiement",ButtonType.EDIT_MODE,()->handleBasculerPaiement());

	}
	
	


	@Override
	protected void drawTable() 
	{
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "nomProducteur", "idPublic", "nomUtilisateur", "prenomUtilisateur", "montant", "paiement"});
		cdesTable.setColumnHeader("nomProducteur","Producteur");
		cdesTable.setColumnHeader("idPublic","Id");
		cdesTable.setColumnHeader("nomUtilisateur","Nom");
		cdesTable.setColumnHeader("prenomUtilisateur","Prénom");
		cdesTable.setColumnHeader("montant","Montant");
		cdesTable.setColumnHeader("paiement","Paiement effectué");
		cdesTable.setColumnAlignment("montant",Align.RIGHT);		

		//
		cdesTable.setConverter("montant", new CurrencyTextFieldConverter());
	}



	@Override
	protected List<CartePrepayeeItemDTO> getLines() 
	{
		Long idProducteur = producteurSelector.getProducteurId();
		Long idUtilisateur = utilisateurSelector.getUtilisateurId();

		// Si le producteur n'est pas défini : la table est vide et les boutons desactivés

/*		if (idProducteur==null )
		{
			return null;
		}
*/		
		return new ProducteurService().getCartePrepayeeList(idProducteur, idUtilisateur);
	}


	@Override
	protected String[] getSortInfos() 
	{
		return new String[] { "idPublic"  };
	}
	
	protected boolean[] getSortAsc()
	{
		return new boolean[] { true  };
	}
	
		

	private void handleCreer()
	{
		Long idProducteur = producteurSelector.getProducteurId();
		Long idUtilisateur = utilisateurSelector.getUtilisateurId();
		if( idProducteur==null  || idUtilisateur==null) {
			MessagePopup m = new MessagePopup("Erreur", ContentMode.HTML, ColorStyle.GREEN, "Veuillez sélectionner un producteur et un amapien" );
			MessagePopup.open(m);
		}
		CartePrepayeeAjout.open(new CartePrepayeeAjout(producteurSelector.getProducteurId(), utilisateurSelector.getUtilisateurId()),this);
	}


	private void handleSupprimer()
	{
		CartePrepayeeItemDTO cppDto = getSelectedLine();
		GestionCartePrepayeeService gcpp = new GestionCartePrepayeeService();
		try {
			gcpp.supprimerCarte(cppDto.getId());
		} catch(UnableToSuppressException utse) {
			MessagePopup m = new MessagePopup("Erreur", ContentMode.HTML, ColorStyle.GREEN, utse.getMessage());
			MessagePopup.open(m);
		}
		refreshTable();
	}

	private void handleBasculerPaiement()
	{
		CartePrepayeeItemDTO cppDto = getSelectedLine();
		GestionCartePrepayeeService gcpp = new GestionCartePrepayeeService();
		gcpp.BasculerPaiementCarte(cppDto.getId());
		refreshTable();
	}

	@Override
	protected String[] getSearchInfos() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onPopupClose()
	{
		refreshTable();
		
	}
}
