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
 package fr.amapj.view.engine.popup.archivagepopup;

import java.util.List;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.TextField;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.service.services.archivage.tools.SuppressionState;
import fr.amapj.service.services.parametres.ParametresArchivageDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Popup generique pour la suppression des elements archivés
 * 
 *
 */
abstract public class SuppressionApresArchivagePopup extends WizardFormPopup
{
	
	/**
	 * Permet de fournir le texte de la première étape : des informations générales sur l'élement à supprimer
	 */
	protected abstract String getInfo();

	/**
	 * Permet de fournir le texte de la deuxième étape : les conditions pour pouvoir supprimer un element quelconque
	 */
	protected abstract String computeSuppressionLib();
	
	/**
	 * Permet de fournir le status de la deuxième étape : le status de l'élement courant pour la suppression
	 */
	protected abstract SuppressionState computeSuppressionState();
	
	/**
	 * Réalise l'archivage final de l'element 
	 */
	protected abstract void suppressElement();
	

	protected ParametresArchivageDTO param;

	private SuppressionState state;

	private TextField tf;

	public enum Step
	{
		INFO , CONDITIONS , CONFIRMATION ;
	}

	/**
	 * 
	 */
	public SuppressionApresArchivagePopup()
	{
		setWidth(60);
		setHeight("50%");
		param = new ParametresService().getParametresArchivage();
		
	}
		
	@Override
	protected void configure()
	{
		add(Step.INFO,()->addFieldInfo());
		add(Step.CONDITIONS,()->addFieldConditions());
		add(Step.CONFIRMATION,()->addFieldConfirmation(),()->checkConfirmation());
	}
	
	
	private void addFieldInfo()
	{
		String str = getInfo();
		addLabel(str, ContentMode.HTML);
	}


	
	private void addFieldConditions()
	{
		String str = computeSuppressionLib();
		addLabel(str, ContentMode.HTML);
		
	}
	
	

	private void addFieldConfirmation()
	{
		state = computeSuppressionState();
		tf=null;
		switch (state.getStatus()) 
		{
		case OUI_SANS_RESERVE:
			addLabel("Ce élément respecte bien toutes les conditions pour pouvoir être supprimé.",ContentMode.HTML);
			addLabel("Veuillez cliquer sur Sauvegarder pour le supprimer, ou Annuler pour ne rien faire.",ContentMode.HTML);
			return;

		case OUI_AVEC_RESERVE_MINEURE:
			addLabel("Ce élément peut être archivé, mais il ne respecte pas les conditions suivantes (réserves mineures) : ",ContentMode.HTML);
			addLabel(asHtmlList(state.reserveMineures),ContentMode.HTML);
			addLabel("Veuillez cliquer sur Sauvegarder pour le supprimer, ou Annuler pour ne rien faire.",ContentMode.HTML);
			return;
			
		case OUI_AVEC_RESERVE_MAJEURE:
			addLabel("Ce élément ne devrait pas être archivé car il ne respecte pas les conditions suivantes (réserves majeures) : ",ContentMode.HTML);
			addLabel(asHtmlList(state.reserveMajeures),ContentMode.HTML);
			addLabel("Néanmoins, si vous pensez que ce élement est bien supprimable, vous pouvez le supprimer quand même en saisissant ci dessous le texte <br/>SUPPRESSION<br/> puis en cliquant sur Sauvegarder",ContentMode.HTML);
			
			tf = new TextField("");
			tf.setValue("");
			tf.setImmediate(true);
			form.addComponent(tf);
			return;
			
		case NON:
			addLabel("Ce élément ne peut pas être supprimé pour les raisons suivantes : ",ContentMode.HTML);
			addLabel(asHtmlList(state.nonSupprimables),ContentMode.HTML);
			setBackOnlyMode();
			return;
			
		default:
			throw new AmapjRuntimeException("state="+state.getStatus());
		}
	}



	private String asHtmlList(List<String> strs) 
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		for (String str : strs) 
		{
			sb.append("<li>");
			sb.append(str);
			sb.append("</li>");
		}
		sb.append("</ul>");
		return sb.toString();
	}
	
	
	private String checkConfirmation() 
	{
		if (tf==null)
		{
			return null;
		}
		
		if (tf.getValue().equals("SUPPRESSION"))
		{
			return null;
		}
		return "Pour pouvoir continuer, vous devez saisir le texte SUPPRESSION (après vous êtes assurés que cet élement est bel et bien supprimable)"; 
	}
	
	

	@Override
	protected void performSauvegarder() throws OnSaveException
	{
		suppressElement();
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
