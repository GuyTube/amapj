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
 package fr.amapj.model.models.contrat.modele;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.fichierbase.Produit;

@Entity
public class ModeleContratProduit implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@ManyToOne
	private ModeleContrat modeleContrat;
	
	@NotNull
	@ManyToOne
	private Produit produit;
	
	@NotNull
	// Numéro d'ordre 
	private int indx;
	
	@NotNull
	// Prix en centimes d'euros, dans l'unité du produit
	private int prix;
	
	// Nombre maximum de produits que le producteur peut livrer à chaque livraison
	private int nbMaxParLivraison;
	
	public enum P implements Mdm
	{
		ID("id") , MODELECONTRAT("modeleContrat") , PRODUIT("produit") , INDX("indx") ;
		
		private String propertyId;   
		   
	    P(String propertyId) 
	    {
	        this.propertyId = propertyId;
	    }
	    public String prop() 
	    { 
	    	return propertyId; 
	    }
		
	} ;


	public Long getId()
	{
		return id;
	}


	public void setId(Long id)
	{
		this.id = id;
	}


	public ModeleContrat getModeleContrat()
	{
		return modeleContrat;
	}


	public void setModeleContrat(ModeleContrat modeleContrat)
	{
		this.modeleContrat = modeleContrat;
	}


	public Produit getProduit()
	{
		return produit;
	}


	public void setProduit(Produit produit)
	{
		this.produit = produit;
	}


	public int getPrix()
	{
		return prix;
	}


	public void setPrix(int prix)
	{
		this.prix = prix;
	}


	public int getNbMaxParLivraison()
	{
		return nbMaxParLivraison;
	}


	public void setNbMaxParLivraison(int nbMaxParLivraison)
	{
		this.nbMaxParLivraison = nbMaxParLivraison;
	}

	public int getIndx()
	{
		return indx;
	}


	public void setIndx(int indx)
	{
		this.indx = indx;
	}
}
