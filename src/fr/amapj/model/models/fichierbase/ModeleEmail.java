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
 package fr.amapj.model.models.fichierbase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.models.param.ModeleEmailEnum;
import fr.amapj.service.services.utilisateur.PreferenceUtilisateurEnum;


@Entity
/**
 * Permet de stocker des modele de Mail pour les notifications par exemple
 *
 */

public class ModeleEmail implements Identifiable
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(length = 255)
	private String titre;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private ModeleEmailEnum designation;
	
	@NotNull
	@Size(min = 1, max = 4096)
	@Column(length = 4096)
	private String contenu;

	
	
	
	// Getters and setters

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public ModeleEmailEnum getDesignation() {
		return designation;
	}

	public void setDesignation(ModeleEmailEnum designation) {
		this.designation = designation;
	}

	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	
	
}
