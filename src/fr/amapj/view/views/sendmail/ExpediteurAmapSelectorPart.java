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

import java.util.List;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.fichierbase.ExpediteurAmap;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.gestioncontratsigne.GestionContratSigneService;
import fr.amapj.service.services.mailer.HistoriqueEmailService;
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
public class ExpediteurAmapSelectorPart
{
	private ComboBox expediteurBox;
	private Long idExpediteurAmap;
	
	/**
	 * 
	 */
	public ExpediteurAmapSelectorPart()
	{
	}
	
	public void disable() {
		expediteurBox.setEnabled(false);
	}

	public void enable() {
		expediteurBox.setEnabled(true);
	}
	
	public HorizontalLayout getChoixExpediteurAmapComponent()
	{
		// Partie choix des souscripteurs de contrat
		HorizontalLayout toolbar1 = new HorizontalLayout();

		Label cLabel = new Label("Expediteur");
		cLabel.setWidth("150px");

		expediteurBox = new ComboBox();
		expediteurBox.setImmediate(true);
		expediteurBox.setWidth("500px");
		expediteurBox.addValueChangeListener(e->handleExpediteurAmapChange());

		toolbar1.addComponent(cLabel);
		toolbar1.addComponent(expediteurBox);
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
		
		List<ExpediteurAmap> eas = new HistoriqueEmailService().getExpediteurAmap(p.userRole);
		for (ExpediteurAmap ea : eas)
		{
			expediteurBox.addItem(ea.getId());
			expediteurBox.setItemCaption(ea.getId(), ea.getDesignation());	
		}
		
	}

	protected void handleReinit()
	{
		expediteurBox.setEnabled(true);
		idExpediteurAmap = null;

	}

	private void handleExpediteurAmapChange()
	{
		idExpediteurAmap = (Long) expediteurBox.getConvertedValue();
	}

	public Long getExpediteurAmapId()
	{
		return idExpediteurAmap;
	}

	public void setExpediteurAmapId( Long id )
	{
		expediteurBox.select(id);
	}

}
