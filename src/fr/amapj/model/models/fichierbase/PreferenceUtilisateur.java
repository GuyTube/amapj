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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.service.services.utilisateur.PreferenceUtilisateurEnum;


@Entity
/**
 * Permet de stocker et lire les préférences des utilisateurs
 *
 */

public class PreferenceUtilisateur implements Identifiable
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull
	@ManyToOne
	private Utilisateur utilisateur;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(length = 255)
	private String nomPref;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(length = 255)
	private String valeurPref;

	
	
	
	// Getters and setters

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}
	
	public Utilisateur getUtilisateur()
	{
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur)
	{
		this.utilisateur = utilisateur;
	}

	public PreferenceUtilisateurEnum getNomPref()
	{
		return PreferenceUtilisateurEnum.getPrefByNom(this.nomPref);
	}

	public void setNomPref(PreferenceUtilisateurEnum pref)
	{
		this.nomPref = pref.getNom();
	}

	public void setNomPref(String prefNom)
	{
		this.nomPref = prefNom;
	}
	
	public String getValeurPref()
	{
		return valeurPref;
	}

	public void setValeurPref(String valeurPref)
	{
		this.valeurPref = valeurPref;
	}
	

	
}
