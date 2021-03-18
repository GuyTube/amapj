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
 package fr.amapj.service.services.mesadhesions;

import java.util.Date;

/**
 *
 */
public class AdhesionDTO
{	
	
	public boolean isModifiable;
	
	// 
	public boolean isSupprimable;
	
	public Long idUtilisateur;
	
	public Long idPeriode;
	
	public String nomPeriode;
	
	public int montantMini;
	
	public int montantConseille;
	
	public Date dateDebut;
	
	public Date dateFin;
	
	public String libCheque;
	
	public Date dateRemiseCheque;
	
	public String textPaiement;
	
	// Si null, alors l'utilisateur n'a pas adhéré
	public Long idPeriodeUtilisateur;
	
	// Montant réellement saisi par l'utilisateur
	public int montantAdhesion;
	
	//
	public Long idBulletin;
	
}
