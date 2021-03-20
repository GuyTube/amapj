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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.contrat.modele.EtatModeleContrat;
import fr.amapj.model.models.editionspe.EditionSpecifique;
import fr.amapj.model.models.param.ChoixOuiNon;
import fr.amapj.model.models.param.EtatModule;

@Entity
@Table( uniqueConstraints=
		{
		   @UniqueConstraint(columnNames={"publicId"})
		})
public class CartePrepayee implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@NotNull
	@Size(min = 1, max = 100)
	@Column(length = 100)
	public String publicId;
	
	//DATEPAIEMENTCARTEPREPAYEE_ID BIGINT,
	
	@Size(min = 0, max = 255)
	@Column(length = 255)
	public String description;
	
	/* Montant de la carte prépayée en cent */
	@NotNull
	public Integer montant;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	public Date dateCreation;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	public Date debutValidite;

	@NotNull
	@Temporal(TemporalType.DATE)
	public Date finValidite;
	
	@NotNull
	@ManyToOne
	public Producteur producteur;
	
	@NotNull
	@ManyToOne
	public Utilisateur utilisateur;
	
	@Temporal(TemporalType.DATE)
	public Date datePaiement;
	
	@Enumerated(EnumType.STRING)
	// Permet de savoir l'état de la carte prépayée
	public EtatCartePrepayee etat = EtatCartePrepayee.ACTIF;
	
	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public void setId(Long id)
	{
		this.id = id;
	}

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMontant() {
		return montant;
	}

	public void setMontant(Integer montant) {
		this.montant = montant;
	}

	public Date getDateCreation() {
		return dateCreation;
	}

	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	public Date getDebutValidite() {
		return debutValidite;
	}

	public void setDebutValidite(Date debutValidite) {
		this.debutValidite = debutValidite;
	}

	public Date getFinValidite() {
		return finValidite;
	}

	public void setFinValidite(Date finValidite) {
		this.finValidite = finValidite;
	}

	public Producteur getProducteur() {
		return producteur;
	}

	public void setProducteur(Producteur producteur) {
		this.producteur = producteur;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}

	public Date getDatePaiement() {
		return datePaiement;
	}

	public void setDatePaiement(Date datePaiement) {
		this.datePaiement = datePaiement;
	}

	public EtatCartePrepayee getEtat() {
		return etat;
	}

	public void setEtat(EtatCartePrepayee etat) {
		this.etat = etat;
	}


}
