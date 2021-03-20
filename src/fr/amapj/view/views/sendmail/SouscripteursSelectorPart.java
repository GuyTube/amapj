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

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.acces.RoleList;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.gestioncontratsigne.GestionContratSigneService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.service.services.session.SessionParameters;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Outil permettant le choix des souscripteurs d'un contrat pour leur envoyer un mail
 * 
 * 
 */
public class SouscripteursSelectorPart
{
	private ComboBox contratBox;
	private Long idModeleContrat;
	
	public enum SpecialValue
	{
		ALL_CONTRATS(new Long(-1), "Tous les souscripteurs de contrats") , 
		TESTMAIL(new Long(-2),"Tester avec mon adresse mail") ;
		
		private Long id;
		private String libelle;
		   
		SpecialValue(Long id, String libelle) 
	    {
	        this.id = id;
	        this.libelle = libelle;
	    }
	    public Long id() 
	    { 
	    	return id; 
	    }
	    public String libelle()
	    {
	    	return libelle;
	    }
		
	}
	/**
	 * 
	 */
	public SouscripteursSelectorPart()
	{
		//this.listener = listener;
		//allowedProducteurs = new AccessManagementService().getAccessLivraisonProducteur(SessionManager.getUserRoles(), SessionManager.getUserId());
		//onlyOneProducteur = (allowedProducteurs.size()==1);
	}

	public HorizontalLayout getChoixContratComponent()
	{
		// Partie choix des souscripteurs de contrat
		HorizontalLayout toolbar1 = new HorizontalLayout();

		Label cLabel = new Label("Destinataires");
		cLabel.setWidth("150px");

		contratBox = new ComboBox();
		contratBox.setImmediate(true);
		contratBox.setWidth("500px");
		contratBox.addValueChangeListener(e->handleContratChange());

		toolbar1.addComponent(cLabel);
		toolbar1.addComponent(contratBox);
		fillAutomaticValues();
		Label tf = new Label("");
		toolbar1.addComponent(tf);
		toolbar1.setExpandRatio(tf, 1);
		toolbar1.setComponentAlignment(tf, Alignment.TOP_RIGHT);
		toolbar1.setSpacing(true);
		toolbar1.setWidth("100%");
		
		return toolbar1;
	}
	
	/**
	 * Doit être appelé à la fin de la construction de la page
	 */
	public void fillAutomaticValues()
	{
		SessionParameters p = SessionManager.getSessionParameters();
		
		List<ModeleContrat> mcs = null;

		if( p.userRole.contains(RoleList.ADMIN) || p.userRole.contains(RoleList.TRESORIER) ) {
			contratBox.addItem(SpecialValue.ALL_CONTRATS.id());
			contratBox.setItemCaption(SpecialValue.ALL_CONTRATS.id(), SpecialValue.ALL_CONTRATS.libelle());	
			contratBox.addItem(SpecialValue.TESTMAIL.id());
			contratBox.setItemCaption(SpecialValue.TESTMAIL.id(), SpecialValue.TESTMAIL.libelle());	
			mcs = new GestionContratSigneService().getModeleContratActif();
		} else if (p.userRole.contains(RoleList.REFERENT)) {
			// Seulement les contrats dont l'utilisateur est référent et pas tout le monde
			contratBox.addItem(SpecialValue.TESTMAIL.id());
			contratBox.setItemCaption(SpecialValue.TESTMAIL.id(), SpecialValue.TESTMAIL.libelle());	
			try {
				mcs = new GestionContratSigneService().getModeleContratReferentActif(p.userId);
			} catch(Exception e) {
				e.printStackTrace();
			}
		} else if (p.userRole.contains(RoleList.PRODUCTEUR)) {
			// Seulement les contrats du producteur et pas tout le monde
			contratBox.addItem(SpecialValue.TESTMAIL.id());
			contratBox.setItemCaption(SpecialValue.TESTMAIL.id(), SpecialValue.TESTMAIL.libelle());	
			mcs = new GestionContratSigneService().getModeleContratProducteurActif(p.userId);
		} else {
			mcs = new ArrayList<ModeleContrat>();
		}
		
		for (ModeleContrat mc : mcs)
		{
			contratBox.addItem(mc.getId());
			contratBox.setItemCaption(mc.getId(), "Souscripteurs du contrat "+mc.getNom());	
		}
		
	}

	protected void handleReinit()
	{
		contratBox.setEnabled(true);
		idModeleContrat = null;

	}

	private void handleContratChange()
	{
		idModeleContrat = (Long) contratBox.getConvertedValue();
	}

	public Long getModeleContratId()
	{
		return idModeleContrat;
	}

}
