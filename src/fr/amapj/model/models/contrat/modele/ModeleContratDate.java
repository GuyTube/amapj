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

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;

@Entity
@Table( uniqueConstraints=
{
   @UniqueConstraint(columnNames={"modeleContrat_id" , "dateLiv"})
})
public class ModeleContratDate implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@NotNull
	@ManyToOne
	public ModeleContrat modeleContrat;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	public Date dateLiv;
	

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

	public Date getDateLiv()
	{
		return dateLiv;
	}

	public void setDateLiv(Date dateLiv)
	{
		this.dateLiv = dateLiv;
	}
	

}
