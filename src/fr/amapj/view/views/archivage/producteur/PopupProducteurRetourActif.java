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
 package fr.amapj.view.views.archivage.producteur;

import com.vaadin.shared.ui.label.ContentMode;

import fr.amapj.model.models.fichierbase.EtatProducteur;
import fr.amapj.service.services.producteur.ProducteurDTO;
import fr.amapj.service.services.producteur.ProducteurService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * 
 *  
 */
public class PopupProducteurRetourActif extends WizardFormPopup
{
	private ProducteurDTO dto;
	
		
	static public enum Step
	{
		SAISIE ;	
	}
	
	
	@Override
	protected void configure()
	{
		add(Step.SAISIE,()->addSaisie());
	}
	
	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
	

	/**
	 * 
	 */
	public PopupProducteurRetourActif(ProducteurDTO dto)
	{
		popupTitle = "Sortir un producteur des archives";
		this.dto = dto;
	}
	
	
	protected void addSaisie()
	{
		addLabel("Le producteur "+dto.nom+" est actuellement à l'état Archivé.", ContentMode.HTML);
		
		addLabel("Avec cet outil, vous allez pouvoir le replacer à l'état ACTIF, ce qui vous permettra de refaire des contrats avec ce producteur.", ContentMode.HTML);
		
		String str = "Etes vous sur de vouloir sortir ce producteur des archives et de le placer à l'état ACTIF ?";
		
		addLabel(str, ContentMode.HTML);
	}
	
	
	protected void performSauvegarder()
	{
		new ProducteurService().updateEtat(dto.id,EtatProducteur.ACTIF);
	}
}
