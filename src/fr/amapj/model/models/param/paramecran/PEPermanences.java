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
 package fr.amapj.model.models.param.paramecran;

import fr.amapj.model.models.param.ChoixOuiNon;
import fr.amapj.model.models.param.paramecran.common.AbstractParamEcran;



/**
 * Parametrage de l'écran liste des adhérents
 */
public class PEPermanences  extends AbstractParamEcran
{
	// Indique si les adhérents peuvent voir le nom des personnes qui seront
	//  permanents dans les différentes listes de la gestion des permanences
	public ChoixOuiNon afficheNomPermanents = ChoixOuiNon.NON;

	public ChoixOuiNon getAfficheNomPermanents() {
		return afficheNomPermanents;
	}

	public void setAfficheNomPermanents(ChoixOuiNon afficheNomPermanents) {
		this.afficheNomPermanents = afficheNomPermanents;
	}
	
}
