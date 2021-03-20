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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.models.acces.RoleList;


@Entity
/**
 * Permet de stocker les messages des utilisateurs
 *
 */

public class HistoriqueEmail implements Identifiable
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	private ExpediteurAmap expediteurAmap;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(length = 255)
	private String roleExpediteur;
	
	@NotNull
	@Size(min = 1, max = 255)
	@Column(length = 255)
	private String sujet;

	@NotNull
	@Size(min = 1, max = 8192)
	@Column(length = 8192)
	private String contenu;
	
	@Size(min = 1, max = 255)
	@Column(length = 255)
	private String userExpediteur;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateHeureEnvoi;
	
	@Size(min = 0, max = 4096)
	@Column(length = 4096)
	private String errorMails;	
	
	@Size(min = 1, max = 255)
	@Column(length = 255)
	private String adresseExpediteur;
	
	// Getters and setters

	public Date getDateHeureEnvoi() {
		return dateHeureEnvoi;
	}

	public void setDateHeureEnvoi(Date dateHeureEnvoi) {
		this.dateHeureEnvoi = dateHeureEnvoi;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ExpediteurAmap getExpediteurAmap() {
		return expediteurAmap;
	}

	public void setExpediteurAmap(ExpediteurAmap expediteurAmap) {
		this.expediteurAmap = expediteurAmap;
	}

	public String getRole() {
		return roleExpediteur;
	}

	public void setRole(String role) {
		this.roleExpediteur = role;
	}

	public String getSujet() {
		return sujet;
	}

	public void setSujet(String sujet) {
		this.sujet = sujet;
	}

	public String getUserExpediteur() {
		return userExpediteur;
	}

	public void setUserExpediteur(String user) {
		this.userExpediteur = user;
	}
	
	public String getContenu() {
		return contenu;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	
	public String getErrorMails() {
		return errorMails;
	}

	public void setErrorMails(String errorMails) {
		this.errorMails = errorMails;
	}
	
	public String getAdresseExpediteur() {
		return adresseExpediteur;
	}

	public void setAdresseExpediteur(String adresseExpediteur) {
		this.adresseExpediteur = adresseExpediteur;
	}

	public String toString() {
		
		StringBuffer res = new StringBuffer(); 
		res.append("HistoriqueEmail id : "+this.getId()).append("\n");
		res.append("Contenu : ").append(this.getContenu()).append("\n");
		res.append("Date envoi : ").append(this.getDateHeureEnvoi()).append("\n");
		String exp = (this.getExpediteurAmap()==null ?"":this.getExpediteurAmap().getDesignation());
		res.append("Expediteur AMAP :").append(exp).append("\n");
		res.append("Role : ").append(this.getRole()).append("\n");
		res.append("Sujet : ").append(this.getSujet()).append("\n");
		res.append("Expediteur : ").append(this.getUserExpediteur()).append("\n");
		res.append("Mails en erreurs : ").append(this.getErrorMails()).append("\n");
		res.append("Adresse mail de l'expediteur : ").append(this.getAdresseExpediteur()).append("\n");
		return res.toString();
	}
}
