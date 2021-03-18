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
 package fr.amapj.view.views.utilisateur;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;

import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.tools.table.complex.ComplexTableBuilder;

/**
 * Permet uniquement de creer des contrats
 * 
 *
 */
public class PopupRendreActifUtilisateurMasse extends WizardFormPopup
{
	private ComplexTableBuilder<UtilisateurDTO> builder;
	
	private List<UtilisateurDTO> toUpdate;

	private List<UtilisateurDTO> initials;

	private boolean actif;
	

	public enum Step
	{
		INFO, SAISIE,CONFIRMATION;
	}

	/**
	 * 
	 */
	public PopupRendreActifUtilisateurMasse(boolean actif)
	{
		this.actif = actif;
		setWidth(60);
		setHeight("100%");
		popupTitle = actif ? "Rendre actifs les utilisateurs en masse" : "Rendre inactifs les utilisateurs en masse";
	}
	
	@Override
	protected void configure()
	{
		add(Step.INFO,()->addFieldInfo());
		add(Step.SAISIE,()->addFieldSaisie(),()->readToArchive());
		add(Step.CONFIRMATION,()->addFieldConfirmation());
		
	}
	
	private void addFieldInfo()
	{	
		String str = 	"Cet outil vous permet de rendre actif ou inactif les utilisateurs en masse. (inactif = archivé)<br/>"+
						"Cet outil ne tient pas compte des règles de gestion des archives."+
						"Dans un cas standard, il n'est pas nécessaire d'utiliser cet outil.";
		addLabel(str, ContentMode.HTML);
	}

	private void addFieldSaisie()
	{		
		initials = new UtilisateurService().getAllUtilisateurs(actif ? EtatUtilisateur.INACTIF : EtatUtilisateur.ACTIF);
		toUpdate = new ArrayList<UtilisateurDTO>();
		
		if (initials.size()==0)
		{
			addLabel("Il n'y a pas d'utilisateurs à modifier.",  ContentMode.HTML);
			setBackOnlyMode();
			return;
		}
		
			
		builder = new ComplexTableBuilder<UtilisateurDTO>(initials);
		builder.setPageLength(20);
		
		builder.addString("Nom de l'utilisateur", false, 300, e->e.nom);
		builder.addString("Prénom de l'utilisateur", false, 300,  e->e.prenom);
		
		builder.addCheckBox(actif ? "Rendre actif cet utilisateur" : "Rendre inactif cet utilisateur", "cb",true, 150, e->false, null);
		
		addComplexTable(builder);
		
		Button b = new Button("Tout cocher",e->handleToutCocher());
		form.addComponent(b);
	}
	
	private void handleToutCocher() 
	{
		for (int i = 0; i < initials.size(); i++)
		{ 
			CheckBox cb = (CheckBox) builder.getComponent(i, "cb");	
			cb.setValue(true);
		}
	}

	private String readToArchive()
	{
		toUpdate = builder.getSelectedCheckBox("cb");
		
		if (toUpdate.size()==0)
		{
			return "Vous devez selectionner au moins un utilisateur pour pouvoir continuer.";
		}
		
		return null;
	}
	
	
	private void addFieldConfirmation()
	{
		// Titre
		setStepTitle("confirmation");
		
		String str = actif ? "rendre actif" : "rendre inactif";
		
		addLabel("Vous allez "+str+" "+toUpdate.size()+" utilisateurs", ContentMode.HTML);
		
		addLabel("Appuyez sur Sauvegarder pour réaliser cette modification, ou Annuler pour ne rien modifier", ContentMode.HTML);
		
	}


	@Override
	protected void performSauvegarder() throws OnSaveException
	{
		EtatUtilisateur status = actif ? EtatUtilisateur.ACTIF : EtatUtilisateur.INACTIF;
		for (UtilisateurDTO p : toUpdate) 
		{
			try
			{
				new UtilisateurService().updateEtat(status,p.id);
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
