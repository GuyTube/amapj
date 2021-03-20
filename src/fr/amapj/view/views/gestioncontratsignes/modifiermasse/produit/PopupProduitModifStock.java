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
 package fr.amapj.view.views.gestioncontratsignes.modifiermasse.produit;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.TextField;

import fr.amapj.model.models.contrat.modele.GestionPaiement;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.LigneContratDTO;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.service.services.gestioncontratsigne.GestionContratSigneService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.tools.table.complex.ComplexTableBuilder;

/**
 * Permet de modifier les quantités disponible pour les produits, même quand des constrats sont signés  
 * 
 *
 */
public class PopupProduitModifStock extends WizardFormPopup
{

	private ModeleContratDTO modeleContrat;
	
	private ComplexTableBuilder<LigneContratDTO> builder;


	public enum Step
	{
		INFO_GENERALES, SAISIE_STOCK , CONFIRMATION;
	}

	/**
	 * 
	 */
	public PopupProduitModifStock(Long mcId)
	{
		setWidth(80);
		popupTitle = "Modification des stocks disponibles des produits d'un contrat";

		// Chargement de l'objet  à modifier
		modeleContrat = new GestionContratService().loadModeleContrat(mcId);

	}
	
	@Override
	protected void configure()
	{
		add(Step.INFO_GENERALES,()->addFieldInfoGenerales());
		add(Step.SAISIE_STOCK,()->addFieldSaisieStock(),()->readPrix());
		add(Step.CONFIRMATION,()->addFieldConfirmation());
	}

	private void addFieldInfoGenerales()
	{
		// Titre
		setStepTitle("les informations générales.");
		
		int nbInscrits = new GestionContratService().getNbInscrits(modeleContrat.id);
		String str;
		
		str = "La modification des stocks n'a pas d'impact sur les contrats déjà existants";
		
		addLabel(str, ContentMode.HTML);
		

	}
	
	

	private void addFieldSaisieStock()
	{
		// Titre
		setStepTitle("les nouveaux stocks");
			
		builder = new ComplexTableBuilder<LigneContratDTO>(modeleContrat.produits);
		builder.setPageLength(7);
		
		builder.addString("Nom du produit", false, 300, e->e.produitNom);
		builder.addString("Conditionnement", false, 300,  e->e.produitConditionnement);
		builder.addInteger("Stock disponible", "stockMax",true , 100,  e->e.nbMaxParLivraison);
		
		addComplexTable(builder);
		
	}
	
	private String readPrix()
	{
		StringBuffer buf = new StringBuffer();
		
		for (int i = 0; i < modeleContrat.produits.size(); i++)
		{
			LigneContratDTO lig = modeleContrat.produits.get(i);
			
			// case du stock 
			TextField tf = (TextField) builder.getComponent(i, "stockMax");
			
			Integer p=null;
			try
			{
				p = (Integer) tf.getConvertedValue();
			}
			catch(Converter.ConversionException e)
			{
				
			}
			lig.nbMaxParLivraison = p;

		}	
		
		
		if (buf.length()!=0)
		{
			return "Merci de corriger les erreurs suivantes:<br/>"+buf.toString();
		}
		else
		{
			return null;
		}
	}
	
	
	private void addFieldConfirmation()
	{
		// Titre
		setStepTitle("confirmation");
		
		addLabel("Appuyez sur Sauvegarder pour réaliser cette modification, ou Annuler pour ne rien modifier", ContentMode.HTML);
		
	}


	@Override
	protected void performSauvegarder()
	{
		new GestionContratSigneService().performModifStock(modeleContrat);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
	
	
	
}
