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
 package fr.amapj.service.services.permanence.periode;

import java.util.Date;

import fr.amapj.model.models.param.ChoixOuiNon;
import fr.amapj.model.models.permanence.periode.EtatPeriodePermanence;
import fr.amapj.view.engine.tools.TableItem;

/**
 * Description legere  d'une periode de permanence (a utiliser uniquement dans PeriodePermanenceListPart)
 *
 */
public class SmallPeriodePermanenceDTO implements TableItem
{
	public Long id;
	
	public EtatPeriodePermanence etat;
	
	public String nom;

	public Date dateDebut;
	
	public Date dateFin;

	public int nbDatePerm;
	
	public int pourcentageInscription;
	
	public ChoixOuiNon limitNbPermanenceUtil;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public EtatPeriodePermanence getEtat()
	{
		return etat;
	}

	public void setEtat(EtatPeriodePermanence etat)
	{
		this.etat = etat;
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public Date getDateDebut()
	{
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut)
	{
		this.dateDebut = dateDebut;
	}

	public Date getDateFin()
	{
		return dateFin;
	}

	public void setDateFin(Date dateFin)
	{
		this.dateFin = dateFin;
	}

	public int getNbDatePerm()
	{
		return nbDatePerm;
	}

	public void setNbDatePerm(int nbDatePerm)
	{
		this.nbDatePerm = nbDatePerm;
	}

	public int getPourcentageInscription()
	{
		return pourcentageInscription;
	}

	public void setPourcentageInscription(int pourcentageInscription)
	{
		this.pourcentageInscription = pourcentageInscription;
	}

	public ChoixOuiNon getLimitNbPermanenceUtil() {
		return limitNbPermanenceUtil;
	}

	public void setLimitNbPermanenceUtil(ChoixOuiNon limitNbPermanenceUtil) {
		this.limitNbPermanenceUtil = limitNbPermanenceUtil;
	}
	
	
	
}
