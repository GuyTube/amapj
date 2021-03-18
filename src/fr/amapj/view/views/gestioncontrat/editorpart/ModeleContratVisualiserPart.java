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
 package fr.amapj.view.views.gestioncontrat.editorpart;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

import fr.amapj.model.models.contrat.modele.GestionPaiement;
import fr.amapj.model.models.contrat.modele.NatureContrat;
import fr.amapj.model.models.param.EtatModule;
import fr.amapj.service.services.gestioncontrat.DateModeleContratDTO;
import fr.amapj.service.services.gestioncontrat.DatePaiementModeleContratDTO;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.LigneContratDTO;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Permet de visualiserles contrats
 */
public class ModeleContratVisualiserPart extends WizardFormPopup
{

	private ModeleContratDTO modeleContrat;

	private Searcher prod;
	

	static public enum Step
	{
		INFO_GENERALES, ACCESS, DATE_LIVRAISON, DATE_FIN_INSCRIPTION , CHOIX_PRODUITS, TYPE_PAIEMENT , DETAIL_PAIEMENT;	
	}
	
	
	@Override
	protected void configure()
	{
		add(Step.INFO_GENERALES, ()->drawInfoGenerales());
		add(Step.ACCESS, ()->drawAccess());
		add(Step.DATE_LIVRAISON, ()->drawDateLivraison());
		add(Step.DATE_FIN_INSCRIPTION, ()->drawFinInscription());
		add(Step.CHOIX_PRODUITS, ()->drawChoixProduits());
		add(Step.TYPE_PAIEMENT , ()->drawTypePaiement());
		add(Step.DETAIL_PAIEMENT , ()->drawDetailPaiement());
	}
	

	/**
	 * 
	 */
	public ModeleContratVisualiserPart(Long id)
	{
		setWidth(80);
		popupTitle = "Visualisation d'un modèle de contrat";

		modeleContrat = new GestionContratService().loadModeleContrat(id);
		item = new BeanItem<ModeleContratDTO>(modeleContrat);
		
		saveButtonTitle = "OK";
	}
	
	

	private void drawInfoGenerales()
	{
		// Titre
		setStepTitle("les informations générales");
		
		addTextField("Nom du contrat", "nom");

		addTextField("Description du contrat", "description");

		addComboEnumField("Nature du contrat", "nature");

		prod = addSearcher("Producteur", "producteur", SearcherList.PRODUCTEUR,null);
		
		addComboEnumField("Fréquence des livraisons", "frequence");
		
		setReadOnlyAll();
	}
	
	protected void drawAccess() 
	{
		setStepTitle("les conditions d'accès à ce contrat");
		
		ParametresDTO param = new ParametresService().getParametres();
		if (param.etatGestionCotisation==EtatModule.INACTIF)
		{
			addLabel("Ce contrat sera accessible par tous les amapiens. Vous pouvez cliquer sur Etape suivante",ContentMode.TEXT);
			return;
		}
		
		//
		addSearcher("Pour pouvoir souscrire à ce contrat, l'amapien doit être inscrit sur la période d'adhésion :", "idPeriodeCotisation", SearcherList.PERIODE_COTISATION, null);
		
		setReadOnlyAll();
	}



	private void drawDateLivraison()
	{
		// Titre
		setStepTitle("les dates de livraison");
	
		//
		addCollectionEditorField("Liste des dates", "dateLivs", DateModeleContratDTO.class);
		addColumn("dateLiv", "Date",FieldType.DATE, null);		
		
		setReadOnlyAll();

	}
	
	
	protected void drawFinInscription()
	{
		if (modeleContrat.nature==NatureContrat.CARTE_PREPAYEE)
		{
			// Titre
			setStepTitle("Contrat Carte prépayée - Délai pour modification du contrat");
			
			addIntegerField("Délai en jour pour modification du contrat avant livraison", "cartePrepayeeDelai");
		}
		else
		{	
			// Titre
			setStepTitle("la date de fin des inscriptions");
			
			// Champ 4
			addDateField("Date de fin des inscriptions", "dateFinInscription");
			
			if (modeleContrat.nature==NatureContrat.ABONNEMENT)
			{
				addLabel("<br/>", ContentMode.HTML);
				addBlocGestionJoker();
			}
		}
		
		setReadOnlyAll();
	}
	
	
	protected void addBlocGestionJoker()
	{
		addComboEnumField("Activer la gestion des jokers", "jokerAutorise");
		
		addIntegerField("Nombre minimum d'absences autorisées pour ce contrat", "jokerNbMin");
		
		addIntegerField("Nombre maximum d'absences autorisées pour ce contrat", "jokerNbMax");
		
		addComboEnumField("Choix des dates de jokers", "jokerMode");
		
		addIntegerField("Délai de prévenance (en jours) pour modifiation des dates jokers", "jokerDelai");
		
	}
	
	



	private void drawChoixProduits()
	{
		// Titre
		setStepTitle("la liste des produits et des prix");
				
		//
		addCollectionEditorField("Produits", "produits", LigneContratDTO.class);
		
		addColumnSearcher("produitId", "Nom du produit",FieldType.SEARCHER, null,SearcherList.PRODUIT,prod);
		addColumn("prix", "Prix du produit", FieldType.CURRENCY, null);	
		
		setReadOnlyAll();
	}


	
	
	

	private void drawTypePaiement()
	{
		if (modeleContrat.nature==NatureContrat.CARTE_PREPAYEE)
		{
			// Titre
			setStepTitle("Contrat Carte prépayée - gestion du paiement");
						
			addLabel("Votre contrat est de type Carte prépayée, il n'y a pas de gestion des paiements possible pour le moment. Vous pouvez juste définir un message avec les indications sur le paiement sur la page suivante.", ContentMode.HTML);
			
		}
		else
		{		
			setStepTitle("genéralités sur le paiement");
						
			addComboEnumField("Gestion des paiements", "gestionPaiement");
		}
		setReadOnlyAll();
	}
	
	
	
	private void drawDetailPaiement()
	{
		setStepTitle("les informations sur le paiement");
		
		
		if (modeleContrat.gestionPaiement==GestionPaiement.GESTION_STANDARD)
		{	
			addTextField("Ordre du chèque", "libCheque");
			
			addDateField("Date limite de remise des chèques", "dateRemiseCheque");
			
			//
			addCollectionEditorField("Liste des dates de paiements", "datePaiements", DatePaiementModeleContratDTO.class);
			addColumn("datePaiement", "Date",FieldType.DATE, null);		
		}
		else
		{
			TextField f = (TextField) addTextField("Texte affiché dans la fenêtre paiement", "textPaiement");
			f.setMaxLength(2048);
			f.setHeight(5, Unit.CM);
		}
		
		setReadOnlyAll();
	}



	@Override
	protected void performSauvegarder()
	{
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
