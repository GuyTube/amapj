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

import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.view.engine.popup.swicthpopup.SwitchPopup;

/**
 * Permet de choisir ce que l'on veut modifier
 * dans le contrat : l'entete, les dates ou les produits
 */
public class ChoixActionUtilisateur extends SwitchPopup
{

	private UtilisateurDTO dto;

	/**
	 * @param dto 
	 * 
	 */
	public ChoixActionUtilisateur(UtilisateurDTO dto)
	{
		this.dto = dto;
		popupTitle = "Autres actions sur les utilisateurs";
		setWidth(50);

	}

	@Override
	protected void loadFollowingPopups()
	{
		line1 = "Veuillez indiquer ce que vous souhaitez faire :";

		if (dto!=null)
		{
			addLine("Supprimer l'utilisateur "+dto.nom+" "+dto.prenom, new PopupSuppressionUtilisateur(dto));
			addSeparator();
		}
		
		addLine("Rendre actif les utilisateurs en masse", new PopupRendreActifUtilisateurMasse(true));
		addLine("Rendre inactif les utilisateurs en masse", new PopupRendreActifUtilisateurMasse(false));
		
		
		addSeparator();
		
		addLine("Envoyer un e mail de bienvenue avec un mot de passe pour tous les utilisateurs sans mot de passe ", new PopupEnvoiPasswordMasse());

	}

}
