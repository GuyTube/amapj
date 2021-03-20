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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.carteprepayee.CartePrepayeeItemDTO;
import fr.amapj.service.services.carteprepayee.GestionCartePrepayeeService;
import fr.amapj.service.services.producteur.ProducteurService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.listpart.ButtonType;
import fr.amapj.view.engine.listpart.StandardListPart;
import fr.amapj.view.engine.popup.corepopup.CorePopup.ColorStyle;
import fr.amapj.view.engine.popup.messagepopup.MessagePopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;
import fr.amapj.view.views.producteur.ProducteurSelectorPart;


/**
 * Affichage des contrats pour un producteur 
 *
 */
public class MesCartesPrepayeesView extends StandardListPart<CartePrepayeeItemDTO>
{
	private ProducteurSelectorPart producteurSelector;
	Label solde;

	private final static Logger logger = LogManager.getLogger();

	public MesCartesPrepayeesView()
	{
		super(CartePrepayeeItemDTO.class,false);
		
	}
	
	
	@Override
	protected String getTitle() 
	{
		return "Mes cartes prépayées";
	}
	
	@Override
	protected void addSelectorComponent()
	{
		producteurSelector = new ProducteurSelectorPart(this);
		addComponent(producteurSelector.getChoixProducteurComponent());
	}


	@Override
	protected void drawButton() 
	{
		addButton("Créer",ButtonType.ALWAYS,()->handleCreer());
		addButton("Supprimer",ButtonType.EDIT_MODE,()->handleSupprimer());

	}
	
	@Override
	protected void addExtraComponent()
	{
		solde = new Label("");
		addComponent(solde);
	}
	
	@Override
	protected void drawTable() 
	{
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "idPublic", "montant", "debutValidite", "finValidite" , "paiement"});
		cdesTable.setColumnHeader("idPublic","Id");
		cdesTable.setColumnHeader("montant","Montant");
		cdesTable.setColumnHeader("debutValidite","Début de validité");
		cdesTable.setColumnHeader("finValidite","Fin de validité");
		cdesTable.setColumnHeader("paiement","Paiement effectué");
		cdesTable.setColumnAlignment("montant",Align.RIGHT);		
        
		//
		cdesTable.setConverter("debutValidite", new DateToStringConverter());
		cdesTable.setConverter("finValidite", new DateToStringConverter());
		cdesTable.setConverter("montant", new CurrencyTextFieldConverter());
		
		refreshSolde();
	}

	protected void refreshSolde() {
		if( producteurSelector != null ) {

			Long idProducteur = producteurSelector.getProducteurId();
			if (idProducteur != null ) {	

				Calendar cal = Calendar.getInstance();
				// Use current year in the request
				int year = cal.get(Calendar.YEAR);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
				dateFormat.setTimeZone(cal.getTimeZone());
				cal.set(year, 0, 1, 2, 0);
				Date debutValid = cal.getTime();
				cal.set(year, 11, 31, 22, 0);
				Date finValid = cal.getTime();
				GestionCartePrepayeeService gcpp = new GestionCartePrepayeeService();
				Integer total = gcpp.getSommeToutesCartePP(idProducteur, SessionManager.getUserId());
				Integer totalDep = gcpp.getSoldeToutesCartePP(idProducteur, SessionManager.getUserId(),debutValid,finValid);
				if( total == null || totalDep == null )
					return;

				float fTotal = (float)total/100;
				float fTotalDep = (float)totalDep/100;
				float fSolde = fTotal-fTotalDep;
				Label newSolde = new Label("Montant total : "+fTotal+" - Total des dépenses : "+fTotalDep+" - Solde : "+fSolde);
				newSolde.setStyleName(ChameleonTheme.LABEL_BIG);

				replaceComponent(solde, newSolde);
				solde = newSolde;

			}

		}

	}

	@Override
	protected List<CartePrepayeeItemDTO> getLines() 
	{
		Long idProducteur = producteurSelector.getProducteurId();
		// Si le producteur n'est pas défini : la table est vide et les boutons desactivés
		if (idProducteur==null)
		{
			return null;
		}
		
		return new ProducteurService().getCartePrepayeeList(idProducteur, SessionManager.getUserId());
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
		CartePrepayeeAjout.open(new CartePrepayeeAjout(producteurSelector.getProducteurId(), SessionManager.getUserId()),this);
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
		refreshSolde();
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
		
		refreshSolde();
		
	}
}
