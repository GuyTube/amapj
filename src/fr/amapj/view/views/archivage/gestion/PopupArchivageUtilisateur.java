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

import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.service.services.archivage.ArchivageUtilisateurService;
import fr.amapj.service.services.parametres.ParametresArchivageDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.tools.table.complex.ComplexTableBuilder;

/**
 * Permet d'archiver les utilisateurs 
 * 
 *
 */
public class PopupArchivageUtilisateur extends WizardFormPopup
{

	private ComplexTableBuilder<UtilisateurDTO> builder;
	
	private List<UtilisateurDTO> toArchive;

	private List<UtilisateurDTO> archivables;

	private ParametresArchivageDTO param;

	public enum Step
	{
		INFO_GENERALES, SAISIE_UTILISATEUR_A_ARCHIVER , CONFIRMATION;
	}

	/**
	 * 
	 */
	public PopupArchivageUtilisateur()
	{
		setWidth(80);
		popupTitle = "Archivage des utilisateurs";
		param = new ParametresService().getParametresArchivage();
		
	}
	
	@Override
	protected void configure()
	{
		add(Step.INFO_GENERALES,()->addFieldInfoGenerales());
		add(Step.SAISIE_UTILISATEUR_A_ARCHIVER,()->addFieldSaisieContrat(),()->readContratsToArchive());
		add(Step.CONFIRMATION,()->addFieldConfirmation());
	}

	private void addFieldInfoGenerales()
	{
		// Titre
		setStepTitle("les informations générales.");
		
		String str = "Cet outil va rechercher la liste des utilisateurs qu'il est souhaitable d'archiver";
		addLabel(str, ContentMode.HTML);
		
		str = new ArchivageUtilisateurService().computeArchivageLib(param);
		addLabel(str, ContentMode.HTML);
		
		str = "Vous pourrez alors choisir dans cette liste ceux que vous voulez archiver et ceux que vous voulez conserver.";
		addLabel(str, ContentMode.HTML);
	}
	
	

	private void addFieldSaisieContrat()
	{
		// Titre
		setStepTitle("les utilisateurs à archiver");
		
		archivables = new ArchivageUtilisateurService().getAllUtilisateursArchivables(param);
		toArchive = new ArrayList<UtilisateurDTO>();
		
		if (archivables.size()==0)
		{
			addLabel("Il n'y a pas d'utilisateurs à archiver.",  ContentMode.HTML);
			setBackOnlyMode();
			return;
		}
		
			
		builder = new ComplexTableBuilder<UtilisateurDTO>(archivables);
		builder.setPageLength(7);
		
		builder.addString("Nom de l'utilisateur", false, 300, e->e.nom);
		builder.addString("Prénom de l'utilisateur", false, 300,  e->e.prenom);
		
		builder.addCheckBox("Archiver cet utilisateur", "cb",true, 150, e->true, null);
		
		addComplexTable(builder);
		
	}
	
	private String readContratsToArchive()
	{
		toArchive = builder.getSelectedCheckBox("cb");
		
		if (toArchive.size()==0)
		{
			return "Vous devez selectionner au moins un utilisateur pour pouvoir continuer.";
		}
		
		return null;
	}
	
	
	private void addFieldConfirmation()
	{
		// Titre
		setStepTitle("confirmation");
		
		addLabel("Vous allez archiver "+toArchive.size()+" utilisateurs", ContentMode.HTML);
		
		addLabel("Appuyez sur Sauvegarder pour réaliser cette modification, ou Annuler pour ne rien modifier", ContentMode.HTML);
		
	}


	@Override
	protected void performSauvegarder() throws OnSaveException
	{
		for (UtilisateurDTO p : toArchive) 
		{
			try
			{
				new UtilisateurService().updateEtat(EtatUtilisateur.INACTIF,p.id);
			}
			catch(Exception e)
			{
				throw new OnSaveException("Impossible d'archiver l'utilisateur "+p.nom+" "+p.prenom+". Raison : "+e.getMessage());
			}
		}
	}

	@Override
	protected Class<?> getEnumClass()
	{
		return Step.class;
	}
}
