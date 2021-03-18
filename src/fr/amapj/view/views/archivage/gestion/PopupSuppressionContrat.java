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
 package fr.amapj.view.views.archivage.gestion;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.label.ContentMode;

import fr.amapj.service.services.archivage.ArchivageContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.service.services.parametres.ParametresArchivageDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.tools.table.complex.ComplexTableBuilder;

/**
 * Permet de supprimer les contrats 
 * 
 *
 */
public class PopupSuppressionContrat extends WizardFormPopup
{

	private ComplexTableBuilder<ModeleContratSummaryDTO> builder;
	
	private List<ModeleContratSummaryDTO> modeleContratToSuppress;

	private List<ModeleContratSummaryDTO> modeleContrats;

	private ParametresArchivageDTO param;

	public enum Step
	{
		INFO_GENERALES, SAISIE_CONTRAT_A_SUPPRIMER , CONFIRMATION;
	}

	/**
	 * 
	 */
	public PopupSuppressionContrat()
	{
		setWidth(80);
		popupTitle = "Suppression des contrats archivés trop anciens";
		param = new ParametresService().getParametresArchivage();
		
	}
	
	@Override
	protected void configure()
	{
		add(Step.INFO_GENERALES,()->addFieldInfoGenerales());
		add(Step.SAISIE_CONTRAT_A_SUPPRIMER,()->addFieldSaisieContrat(),()->readContratsToArchive());
		add(Step.CONFIRMATION,()->addFieldConfirmation());
	}

	private void addFieldInfoGenerales()
	{
		// Titre
		setStepTitle("les informations générales.");
		
		String str = "Cet outil va rechercher la liste des contrats qu'il est souhaitable de supprimer";
		addLabel(str, ContentMode.HTML);
		
		str = new ArchivageContratService().computeSuppressionLib(param);
		addLabel(str, ContentMode.HTML);
		
		str = "Vous pourrez alors choisir dans cette liste ceux que vous voulez archiver et ceux que vous voulez conserver.";
		addLabel(str, ContentMode.HTML);
	}
	
	

	private void addFieldSaisieContrat()
	{
		// Titre
		setStepTitle("les contrats à supprimer");
		
		modeleContrats = new ArchivageContratService().getAllContratSupprimables(param);
		modeleContratToSuppress = new ArrayList<ModeleContratSummaryDTO>();
		
		if (modeleContrats.size()==0)
		{
			addLabel("Il n'y a pas de contrats à supprimer.",  ContentMode.HTML);
			setBackOnlyMode();
			return;
		}
		
			
		builder = new ComplexTableBuilder<ModeleContratSummaryDTO>(modeleContrats);
		builder.setPageLength(7);
		
		builder.addString("Nom du contrat", false, 300, e->e.nom);
		builder.addString("Nom du producteur", false, 300,  e->e.nomProducteur);
		builder.addDate("Première livraison", false, 150,  e->e.dateDebut);
		builder.addDate("Dernière livraison", false, 150,  e->e.dateFin);
		
		builder.addCheckBox("Supprimer ce contrat", "cb",true, 150, e->true, null);
		
		addComplexTable(builder);
		
	}
	
	private String readContratsToArchive()
	{
		modeleContratToSuppress = builder.getSelectedCheckBox("cb");
		
		if (modeleContratToSuppress.size()==0)
		{
			return "Vous devez selectionner au moins un contrat pour pouvoir continuer.";
		}
		
		return null;
	}
	
	
	private void addFieldConfirmation()
	{
		// Titre
		setStepTitle("confirmation");
		
		addLabel("Vous allez supprimer DEFINITIVEMENT "+modeleContratToSuppress.size()+" contrats", ContentMode.HTML);
		
		addLabel("Appuyez sur Sauvegarder pour réaliser cette modification, ou Annuler pour ne rien modifier", ContentMode.HTML);
		
	}


	@Override
	protected void performSauvegarder() throws OnSaveException
	{
		for (ModeleContratSummaryDTO mc : modeleContratToSuppress) 
		{
			try
			{
				new ArchivageContratService().deleteModeleContratAndContrats(mc.id);
			}
			catch(Exception e)
			{
				throw new OnSaveException("Impossible de supprimer le contrat "+mc.nom+". Raison : "+e.getMessage());
			}
		}
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
