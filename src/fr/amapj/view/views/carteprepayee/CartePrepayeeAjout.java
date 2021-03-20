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

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.model.models.param.ChoixOuiNon;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.access.AdminTresorierDTO;
import fr.amapj.service.services.carteprepayee.CartePrepayeeItemDTO;
import fr.amapj.service.services.carteprepayee.GestionCartePrepayeeService;
import fr.amapj.service.services.gestioncontrat.DateModeleContratDTO;
import fr.amapj.service.services.mescontrats.MesCartesPrepayeesService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.engine.popup.formpopup.validator.NotNullValidator;
import fr.amapj.view.engine.tools.BaseUiTools;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Permet d'ajouter une nouvelle carte prépayée
 * 
 *
 */
public class CartePrepayeeAjout extends FormPopup
{

	private CartePrepayeeItemDTO dto;

	private Long idProducteur;

	private Long idUtilisateur;

	
	/**
	 * 
	 */
	public CartePrepayeeAjout(Long idProducteur, Long idUtilisateur)
	{
		this.idProducteur = idProducteur;
		this.idUtilisateur = idUtilisateur;
		dto = new CartePrepayeeItemDTO();
		Calendar cal = Calendar.getInstance();
		dto.setAnnee(cal.get(Calendar.YEAR));
		setWidth(80);
		

		popupTitle = "Achat d'une carte prépayée";
		
		
		item = new BeanItem<CartePrepayeeItemDTO>(this.dto);

	}
	
	
	@Override
	protected void addFields()
	{
		BaseUiTools.addHtmlLabel(form,"Veuillez saisir le montant de la carte prépayée que vous souhaitez acheter", "");
		addGeneralComboField("Année",new DateCombo(dto.getAnnee(),false).getAnnees(),"annee", null);
		// Champ 1
		//addSearcher( "Utilisateur", "utilisateurId", SearcherList.UTILISATEUR_TOUS, null,new NotNullValidator());
		addCurrencyField("Montant", "montant", true);

	}



	private class DateCombo {
		private ArrayList<Integer> annees;
			
		public DateCombo(int year,boolean isAdmin) {
			annees = new ArrayList<Integer>();
			if(isAdmin)
				annees.add(year - 1);
			annees.add(year);
			if(isAdmin)
				annees.add(year + 1);
		}
		ArrayList<Integer> getAnnees() {
			return annees;
		}
		
		void setAnnees(ArrayList<Integer> annees) {
			this.annees = annees;
		}
		

	}
	@Override
	protected void performSauvegarder() throws OnSaveException
	{
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		dateFormat.setTimeZone(cal.getTimeZone());
		String dateCourante = dateFormat.format(cal.getTime());
		cal.set(dto.getAnnee(), 0, 1, 2, 0);
		dto.setDebutValidite(cal.getTime());
		cal.set(dto.getAnnee(), 11, 31, 22, 0);
		dto.setFinValidite(cal.getTime());
		dto.setDescription("");
		StringBuilder sb = new StringBuilder(SessionManager.getUserId().toString());
		String publicId = "1"+sb.reverse()+dateCourante;
		dto.setIdPublic(new BigInteger(publicId).toString(36));
		dto.setPaiement(ChoixOuiNon.NON);
		dto.setIdProducteur(idProducteur);
		dto.setIdUtilisateur(idUtilisateur);
		new GestionCartePrepayeeService().creerCarte(true,dto);
	}
	

}
