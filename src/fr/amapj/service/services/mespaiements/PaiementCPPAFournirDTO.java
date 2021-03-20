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
 package fr.amapj.service.services.mespaiements;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;

/**
 * 
 *
 */
public class PaiementCPPAFournirDTO
{
	
	// Nom du contrat
	public String numPublicCartePP;

	// Ordre du cheque
	public String libCheque;
	
	// Date limite de remise des paiements 
	public Date dateRemise; 
	
	// Liste de paiements
	public Integer montant;
		
	public String formatPaiement()
	{
		String mt = new CurrencyTextFieldConverter().convertToString(montant)+" €";
		String str ;
		str = "1 chèque de "+mt+" à fournir au plus tard lors de la prochaine distribution d'un contrat prépayé";
		return str;
	}
}
