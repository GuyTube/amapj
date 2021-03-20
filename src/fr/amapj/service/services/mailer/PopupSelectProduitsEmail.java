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
 package fr.amapj.service.services.mailer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.ComboBox;

import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.model.models.param.ChoixOuiNon;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.service.services.meslivraisons.ProducteurLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.QteProdDTO;
import fr.amapj.view.engine.enumselector.EnumSearcher;
import fr.amapj.view.engine.grid.GridHeaderLine;
import fr.amapj.view.engine.grid.GridSizeCalculator;
import fr.amapj.view.engine.grid.booleangrid.PopupBooleanGrid;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.views.utilisateur.PopupEnvoiEmail;

/**
 * Popup pour sélectionner les mails à envoyer en fonction des produits
 * aui doivent être livrés à une date donnée. Permet de prévenir 
 * les amapiens d'un problème de production ou autre 
 *  
 */
public class PopupSelectProduitsEmail extends PopupBooleanGrid implements PopupListener
{
	
	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
	
	private ContratDTO contratDTO;
		
	private Long idModeleContrat;
		
	private Long idModeleContratDate;
	
	private ProducteurLivraisonsDTO producteurLivraison;
	/**
	 * 
	 */
	public PopupSelectProduitsEmail(ProducteurLivraisonsDTO prodLiv)
	{
		super();
		this.idModeleContrat = prodLiv.idModeleContrat;
		this.idModeleContratDate = prodLiv.idModeleContratDate;
		this.saveButtonTitle = "Continuer ...";
		this.producteurLivraison = prodLiv;
	}
	
	
	
	public void loadParam()
	{
		// Chargement de l'objet à modifier
		contratDTO = new MesContratsService().loadContrat(idModeleContrat,null);
		//contratDTO.expandExcluded();
		
		//
		popupTitle = "Selectionner les destinataires par les produits qu'il doivent récupérer";
		setWidth(90);
		
		//
		param.messageSpecifique = "<b>Cet écran vous permet de sélectionner les produits pour lesquels vous souhaitez<br/>"
				+ "envoyer un email ciblé aux consommateurs concernés par un plusieurs produits de cette livraison.<br/><br/>"
				+ "Sélectionnez les produits concernés par l'annonce puis cliquer sur 'Continuer...'<br/><br/><br/></b>";
		
		param.nbCol = producteurLivraison.produits.size();
		param.nbLig = 1;
		param.box = new boolean[1][producteurLivraison.produits.size()];
		param.largeurCol = 110;
		
				
		// Construction du header 1
		GridHeaderLine line1  =new GridHeaderLine();
		line1.styleName = "tete";
		line1.cells.add("Produit");
				
		for (QteProdDTO produit: producteurLivraison.produits)
		{
			line1.cells.add(produit.nomProduit);
		}
		GridSizeCalculator.autoSize(line1,param.largeurCol,"Arial",16);
		
		param.headerLines.add(line1);
		GridSizeCalculator.autoSize(line1,param.largeurCol,"Arial",16);
		
		
		// Partie gauche du tableau
		param.leftPartLine.add("Cocher les produits");
	}
	
	public void performSauvegarder()
	{
		int cpt = 0;
		List<Long> produits = new ArrayList<Long>();
		for (QteProdDTO produit: producteurLivraison.produits)
		{
			if( param.box[0][cpt] ) {
				produits.add(produit.idProduit);
			}
			cpt++;
		}
		if( produits.size() > 0 ) {
			List<Utilisateur> users = new GestionContratService().getUtilisateursProduit(idModeleContratDate, produits);
			FormPopup.open(new PopupEnvoiEmail(users),this);

		}
		
	}



	@Override
	public void onPopupClose() {
		// TODO Auto-generated method stub
		
	}

}
