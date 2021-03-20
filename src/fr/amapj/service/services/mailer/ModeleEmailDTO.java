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

import fr.amapj.model.models.param.ModeleEmailEnum;

/**
 * Permet la gestion des modèles de mails
 * 
 */
public class ModeleEmailDTO 
{
	public Long id;

	public String titreEmail;
	
	public String contenuEmail;
	
	public ModeleEmailEnum designation;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitreEmail() {
		return titreEmail;
	}

	public void setTitreEmail(String titreEmail) {
		this.titreEmail = titreEmail;
	}

	public String getContenuEmail() {
		return contenuEmail;
	}

	public void setContenuEmail(String contenuEmail) {
		this.contenuEmail = contenuEmail;
	}

	public ModeleEmailEnum getDesignation() {
		return designation;
	}

	public void setDesignation(ModeleEmailEnum designation) {
		this.designation = designation;
	}

}
