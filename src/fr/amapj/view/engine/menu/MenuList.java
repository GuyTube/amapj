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
 package fr.amapj.view.engine.menu;


import com.vaadin.server.FontAwesome;
import com.vaadin.server.FontIcon;

import fr.amapj.model.models.acces.RoleList;

/**
 * Contient la liste des Menus disponibles dans l'application
 * 
 */
public enum MenuList
{ 	
	
	// Partie standard
	
	MES_CONTRATS("Mes contrats",FontAwesome.FILE_TEXT_O),
	
	MES_LIVRAISONS("Mes livraisons",FontAwesome.CUTLERY),
	
	MES_PAIEMENTS("Mes paiements",FontAwesome.EURO),
	
	MES_ADHESIONS("Mes adhésions",FontAwesome.CERTIFICATE),
	
	MON_COMPTE("Mon compte",FontAwesome.USER),
	
	VISITE_AMAP("Visite AMAP",FontAwesome.DELICIOUS),
	
	LISTE_PRODUCTEUR_REFERENT("Producteurs / Référents",FontAwesome.LEAF),
	
	LISTE_ADHERENTS("Liste des adhérents",FontAwesome.USERS),
	
	MES_PERMANENCES("Mes permanences",FontAwesome.CALENDAR_O),
	
	HISTORIQUE_CONTRATS("Historique de mes contrats",FontAwesome.BOOK),
	
	HISTORIQUE_PAIEMENTS("Historique de mes paiements",FontAwesome.EURO),
	
	
	// Partie producteur
	
	LIVRAISONS_PRODUCTEUR("Livraisons d'un producteur",FontAwesome.TRUCK) ,
	
	CONTRATS_PRODUCTEUR("Contrats d'un producteur",FontAwesome.FILE_TEXT_O),
	
	
	// Partie référents
	
	GESTION_CONTRAT("Gestion des contrats vierges",FontAwesome.FOLDER_OPEN_O),
	
	GESTION_CONTRAT_SIGNES("Gestion des contrats signés",FontAwesome.FILE_TEXT_O),
	
	RECEPTION_CHEQUES("Réception des chèques",FontAwesome.EURO),
	
	REMISE_PRODUCTEUR("Remise aux producteurs",FontAwesome.MONEY),
	
	PRODUIT("Gestion des produits",FontAwesome.LEAF) ,
	
	SAISIE_PLANNING_DISTRIBUTION("Planification des permanences",FontAwesome.CALENDAR_O) ,
	
	CONTRATS_AMAPIEN("Contrats d'un amapien",FontAwesome.FILE_TEXT_O) ,
	
	LIVRAISON_AMAPIEN("Livraisons d'un amapien",FontAwesome.CUTLERY) ,
	
	SYNTHESE_MULTI_CONTRAT("Synthèses multi contrats",FontAwesome.BAR_CHART_O),
	
	
	// Partie permanence
	
	PERIODE_PERMANENCE("Périodes de permanence",FontAwesome.CALENDAR_O) ,
	
	DETAIL_PERIODE_PERMANENCE("Gestion des inscriptions aux permanences",FontAwesome.CALENDAR_O) ,
	
	ROLE_PERMANENCE("Rôles de permanence",FontAwesome.CALENDAR_O) ,
	
	// Partie trésorier
	
	UTILISATEUR("Gestion des utilisateurs",FontAwesome.USERS) , 
	
	PRODUCTEUR("Gestion des producteurs",FontAwesome.LEAF) ,
	
	BILAN_COTISATION("Bilan des adhésions",FontAwesome.TABLE) ,
	
	RECEPTION_COTISATION("Réception des adhésions",FontAwesome.CHECK_SQUARE_O) ,
	
	IMPORT_DONNEES("Import des données",FontAwesome.CLOUD_DOWNLOAD),
	
	LISTE_TRESORIER("Liste des trésoriers",FontAwesome.GRADUATION_CAP),
	
	ETIQUETTE("Editions spécifiques",FontAwesome.PRINT),
	
	// Partie archives
	
	CONTRAT_ARCHIVE("Contrats archivés",FontAwesome.ARCHIVE),
	
	PRODUCTEUR_ARCHIVE("Producteurs archivés",FontAwesome.ARCHIVE),
	
	UTILISATEUR_ARCHIVE("Utilisateurs archivés",FontAwesome.ARCHIVE),
	
	GESTION_ARCHIVE("Gestion des archives",FontAwesome.ARCHIVE),  
	
	// Partie admnistrateur
	
	PARAMETRES("Paramètres généraux",FontAwesome.COG),
	
	LISTE_ADMIN("Liste des administrateurs",FontAwesome.GRADUATION_CAP),
	
	MAINTENANCE("Maintenance",FontAwesome.TACHOMETER),
	
	ENVOI_MAIL("Envoyer un mail",FontAwesome.ENVELOPE_O),
	
	// Partie Saas

	LISTE_APP_INSTANCE("Liste des instances",FontAwesome.TASKS),
	
	SUIVI_ACCES("Suivi accès",FontAwesome.SIGN_IN), 
	
	VISU_LOG("Visualisation des logs",FontAwesome.TAGS),
	
	STAT_ACCES("Statistiques des accès",FontAwesome.BAR_CHART_O) , 
	
	SUPERVISION("Supervision",FontAwesome.HEART) ,
	
	OUTILS_DEV("Outils developpement",FontAwesome.EDIT) ,
	
	// Partie non visible, mais paramétrable
	
	OUT_SAISIE_PAIEMENT("Saisie des paiements par l'amapien",FontAwesome.GEAR)
	
	
	;

	
	private String title;   
	private FontIcon font;
	   
	MenuList(String title,FontIcon font) 
    {
        this.title = title;
        this.font = font;
    }
	
	
    public String getTitle() 
    { 
    	return title; 
    }
    
    
    public FontIcon getFont() 
    { 
    	return font;
    }
    
	
}
