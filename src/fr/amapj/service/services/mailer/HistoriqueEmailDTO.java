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

import java.util.Date;

import fr.amapj.view.engine.tools.TableItem;

/**
 * Represente un contrat signe
 *
 */
public class HistoriqueEmailDTO implements TableItem
{
	public Long id;

	public String expediteur;
	
	public String role;
	
	public String sujet;

	public Date dateEnvoi;
	
	public String contenu;

	public String errorMails;
	
	public Long expediteurId;
	
	public String adresseExpediteur;
	
	public String mixExpediteur;
	
	public String hasEmailErreur;
	
	public String getHasEmailErreur() {
		return hasEmailErreur;
	}

	public void setHasEmailErreur(String hasEmailErreur) {
		this.hasEmailErreur = hasEmailErreur;
	}

	public String getMixExpediteur() {
		return mixExpediteur;
	}

	public void setMixExpediteur(String mixExpediteur) {
		this.mixExpediteur = mixExpediteur;
	}

	public Long getExpediteurId() {
		return expediteurId;
	}

	public void setExpediteurId(Long expediteurId) {
		this.expediteurId = expediteurId;
	}

	public String getErrorMails() {
		return errorMails;
	}

	public void setErrorMails(String errorMails) {
		this.errorMails = errorMails;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getExpediteur() {
		return expediteur;
	}

	public void setExpediteur(String expediteur) {
		this.expediteur = expediteur;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	
	public String getSujet() {
		return sujet;
	}

	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	public Date getDateEnvoi() {
		return dateEnvoi;
	}

	public void setDateEnvoi(Date dateEnvoi) {
		this.dateEnvoi = dateEnvoi;
	}

	public String getAdresseExpediteur() {
		return adresseExpediteur;
	}

	public void setAdresseExpediteur(String adresseExpediteur) {
		this.adresseExpediteur = adresseExpediteur;
	}

}
