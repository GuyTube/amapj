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
 package fr.amapj.service.services.carteprepayee;

import java.util.Date;

import fr.amapj.model.models.param.ChoixOuiNon;
import fr.amapj.view.engine.tools.TableItem;


/**
 * Informations sur une carte prépayée
 *
 */
public class CartePrepayeeItemDTO  implements TableItem
{
	
	// 
	public Long id;
	
	public String idPublic;
	
	public Integer montant;
	
	public Date debutValidite;
	
	public Date finValidite;
	
	public String description;
	
	public ChoixOuiNon paiement;
	
	public Long idUtilisateur;
	
	public Long idProducteur;
	
	public String nomUtilisateur;
	
	public String prenomUtilisateur;
	
	public String nomProducteur;
	
	public Integer annee;
	
		

	public Integer getAnnee() {
		return annee;
	}

	public void setAnnee(Integer annee) {
		this.annee = annee;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIdPublic() {
		return idPublic;
	}

	public void setIdPublic(String idPublic) {
		this.idPublic = idPublic;
	}

	public Integer getMontant() {
		return montant;
	}

	public void setMontant(Integer montant) {
		this.montant = montant;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ChoixOuiNon getPaiement() {
		return paiement;
	}

	public void setPaiement(ChoixOuiNon paiement) {
		this.paiement = paiement;
	}

	public Long getIdUtilisateur() {
		return idUtilisateur;
	}

	public void setIdUtilisateur(Long idUtilisateur) {
		this.idUtilisateur = idUtilisateur;
	}

	public Long getIdProducteur() {
		return idProducteur;
	}

	public void setIdProducteur(Long idProducteur) {
		this.idProducteur = idProducteur;
	}

	public String getNomUtilisateur() {
		return nomUtilisateur;
	}

	public void setNomUtilisateur(String nomUtilisateur) {
		this.nomUtilisateur = nomUtilisateur;
	}

	public String getPrenomUtilisateur() {
		return prenomUtilisateur;
	}

	public void setPrenomUtilisateur(String prenomUtilisateur) {
		this.prenomUtilisateur = prenomUtilisateur;
	}

	public String getNomProducteur() {
		return nomProducteur;
	}

	public void setNomProducteur(String nomProducteur) {
		this.nomProducteur = nomProducteur;
	}

}
