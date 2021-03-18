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
 package fr.amapj.view.views.cotisation.reception;

import java.util.List;

import com.vaadin.ui.Table.Align;

import fr.amapj.service.services.edgenerator.excel.EGBilanAdhesion;
import fr.amapj.service.services.edgenerator.pdf.PGBulletinAdhesion;
import fr.amapj.service.services.gestioncotisation.GestionCotisationService;
import fr.amapj.service.services.gestioncotisation.PeriodeCotisationDTO;
import fr.amapj.service.services.gestioncotisation.PeriodeCotisationUtilisateurDTO;
import fr.amapj.service.services.mesadhesions.MesAdhesionsService;
import fr.amapj.view.engine.excelgenerator.TelechargerPopup;
import fr.amapj.view.engine.listpart.ButtonType;
import fr.amapj.view.engine.listpart.StandardListPart;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;
import fr.amapj.view.views.cotisation.PeriodeCotisationSelectorPart;

/**
 * Gestion de la réception des cotisations
 */
@SuppressWarnings("serial")
public class ReceptionCotisationView extends StandardListPart<PeriodeCotisationUtilisateurDTO>
{
	private PeriodeCotisationSelectorPart periodeSelector;
	
	public ReceptionCotisationView()
	{
		super(PeriodeCotisationUtilisateurDTO.class,false);
		periodeSelector = new PeriodeCotisationSelectorPart(this);
	}
	
	
	@Override
	protected String getTitle() 
	{
		return "Réception des adhésions";
	}
	
	@Override
	protected void addSelectorComponent()
	{
		addComponent(periodeSelector.getChoixPeriodeComponent());	
	}
	

	@Override
	protected void drawButton() 
	{
		addButton("Ajouter une adhésion",ButtonType.ALWAYS,e->new PopupAjoutCotisation(periodeSelector.getPeriodeId()));
		addButton("Réceptionner/Modifier une adhésion",ButtonType.EDIT_MODE,e->new PopupModifCotisation(e));
		addButton("Supprimer une adhésion",ButtonType.EDIT_MODE,e->handleSupprimer(e));
		addButton("Télécharger",ButtonType.ALWAYS,e->handleTelecharger(e));

		
		addButton("Ajouter en masse",ButtonType.ALWAYS,e->new AjouterEnMasseAdhesion(periodeSelector.getPeriodeId()));
		addButton("Réceptionner en masse",ButtonType.ALWAYS,e->new ReceptionnerEnMasseAdhesion(periodeSelector.getPeriodeId()));
		
		addSearchField("Rechercher par nom");
		
	}


	@Override
	protected void drawTable() 
	{
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "nomUtilisateur", "prenomUtilisateur","dateAdhesion" ,"dateReceptionCheque" , "montantAdhesion" ,"etatPaiementAdhesion","typePaiementAdhesion"});
		
		cdesTable.setColumnHeader("nomUtilisateur","Nom ");
		cdesTable.setColumnHeader("prenomUtilisateur","Prénom");
		cdesTable.setColumnHeader("dateAdhesion","Date de l'adhésion");
		cdesTable.setColumnHeader("dateReceptionCheque","Date réception chéque");
		
		cdesTable.setColumnHeader("montantAdhesion","Montant adhésion (en €)");
		cdesTable.setColumnAlignment("montantAdhesion",Align.RIGHT);
		
		cdesTable.setColumnHeader("etatPaiementAdhesion","Etat du paiement");
		cdesTable.setColumnHeader("typePaiementAdhesion","Type de paiement");
		
		
		cdesTable.setConverter("montantAdhesion", new CurrencyTextFieldConverter());
		cdesTable.setConverter("dateAdhesion", new DateToStringConverter());
		cdesTable.setConverter("dateReceptionCheque", new DateToStringConverter());
	}



	@Override
	protected List<PeriodeCotisationUtilisateurDTO> getLines() 
	{
		Long idPeriode = periodeSelector.getPeriodeId();
		if (idPeriode==null)
		{
			return null;
		}
		return new GestionCotisationService().loadBilanAdhesion(idPeriode).utilisateurDTOs;
	}


	@Override
	protected String[] getSortInfos() 
	{
		return new String[] { "nomUtilisateur", "prenomUtilisateur"};
	}
	
	protected String[] getSearchInfos()
	{
		return new String[] { "nomUtilisateur", "prenomUtilisateur" };
	}

	
	private CorePopup handleSupprimer(PeriodeCotisationUtilisateurDTO dto)
	{
		String text = "Etes vous sûr de vouloir supprimer l'adhésion de "+dto.nomUtilisateur+" "+dto.prenomUtilisateur+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,dto.id,e->new MesAdhesionsService().deleteAdhesion(e,true));
		return confirmPopup;
	}
	
	private TelechargerPopup handleTelecharger(PeriodeCotisationUtilisateurDTO dto)
	{
		Long idPeriode = periodeSelector.getPeriodeId();
		boolean hasBulletin = new GestionCotisationService().load(idPeriode).idBulletinAdhesion!=null;
		
		TelechargerPopup popup = new TelechargerPopup("Réception des adhésions");
		if (dto!=null && hasBulletin)
		{
			popup.addGenerator(new PGBulletinAdhesion(idPeriode, dto.id, null));
			popup.addSeparator();
		}
		
		popup.addGenerator(new EGBilanAdhesion(idPeriode));
		if (hasBulletin)
		{
			popup.addGenerator(new PGBulletinAdhesion(idPeriode, null, null));
		}
		return popup;
	}
}
