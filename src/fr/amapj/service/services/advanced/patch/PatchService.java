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
 package fr.amapj.service.services.advanced.patch;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.amapj.common.DateUtils;
import fr.amapj.model.engine.tools.SpecificDbUtils;
import fr.amapj.model.engine.transaction.DbUtil;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Utilisateur;

/**
 * Permet la gestion des pacths pour les migrations 
 */
public class PatchService
{
	
	private final static Logger logger = LogManager.getLogger();
	
	public PatchService()
	{

	}


//	/**
//	 * Application du patch V019
//	 */
//	public String applyPatchV019()
//	{
//		StringBuffer str = new StringBuffer();
//		SpecificDbUtils.executeInAllDb(()->patch(str),false);
//		return str.toString();
//	}
//	
//	@DbWrite
//	private Void patch(StringBuffer str)
//	{
//		EntityManager em = TransactionHelper.getEm();
//		
//		String dbName = DbUtil.getCurrentDb().getDbName();
//		
//		Query q = em.createQuery("select p from EditionSpecifique p");
//
//		List<EditionSpecifique> ps = q.getResultList();
//		for (EditionSpecifique p : ps)
//		{
//			zipContent(p);
//		}
//		
//		str.append("ok pour "+dbName+"<br/>");
//		
//		return null;
//	}
//
//
//	/**
//	 * On zippe uniquement si cela n'a pas déja été fait 
//	 * @param p
//	 */
//	private void zipContent(EditionSpecifique p)
//	{
//		if (p.content==null)
//		{
//			return ;
//		}
//		if (p.content.startsWith("{")==false)
//		{
//			return;
//		}
//		
//		p.content = GzipUtils.compress(p.content);
//	}

	
//	/**
//	 * Application du patch V020
//	 */
//	public String applyPatchV020()
//	{
//		StringBuffer str = new StringBuffer();
//		SpecificDbUtils.executeInAllDb(()->patch(str),false);
//		return str.toString();
//	}
//	
//	@DbWrite
//	private Void patch(StringBuffer str)
//	{
//		EntityManager em = TransactionHelper.getEm();
//		
//		String dbName = DbUtil.getCurrentDb().getDbName();
//		
//		int nb= 0;
//		
//		str.append("Nombre de données transférées="+nb+" - ok pour "+dbName+"<br/>");
//		
//		logger.info("Nombre de données transférées="+nb+" - ok pour "+dbName);
//		
//		return null;
//	}
	
	
	/**
	 * Application du patch V026
	 *
	 */
	public String applyPatchV026()
	{
		StringBuffer str = new StringBuffer();
		SpecificDbUtils.executeInAllDb(()->patch(str),false);
		return str.toString();
	}
	
	@DbWrite
	private Void patch(StringBuffer str)
	{
		EntityManager em = TransactionHelper.getEm();
		
		String dbName = DbUtil.getCurrentDb().getDbName();
		
		int nb = processProducteur(em);		
		String msg = "Nombre de producteur trouvés ="+nb+" - ok pour "+dbName;
		
		str.append(msg+"<br/>");
		logger.info(msg);
		
		nb = processUtilisateur(em);		
		msg = "Nombre de utilisateurs trouvés ="+nb+" - ok pour "+dbName;
		
		str.append(msg+"<br/>");
		logger.info(msg);
		
		return null;
	}

	/**
	 * Ajout d'une date de creation pour les producteurs
	 */
	private int processProducteur(EntityManager em) 
	{
		TypedQuery<Producteur> q = em.createQuery("select p from Producteur p ORDER BY p.id DESC",Producteur.class);
		List<Producteur> ps = q.getResultList();
		
		for (int i = 0; i < ps.size(); i++) 
		{
			Producteur producteur = ps.get(i);
			
			// on détermine la plus vielle date de livraison d'un producteur
			Date creation = findDateCreationProdcuteur(producteur,em);
			
			// Si elle existe, on l'utilise
			if (creation!=null)
			{
				producteur.dateCreation = creation;
			}
			// Sinon on utilise celle du producteur ayant l'id juste plus grand (précédent)  
			else
			{
				if (i!=0)
				{
					producteur.dateCreation = ps.get(i-1).dateCreation;
				}
			}
		}
		
		
		return ps.size();
	}


	private Date findDateCreationProdcuteur(Producteur producteur, EntityManager em) 
	{
		TypedQuery<Date> q = em.createQuery("select p.dateCreation from Contrat p where p.modeleContrat.producteur=:p ORDER BY p.dateCreation",Date.class);
		q.setParameter("p", producteur);
		List<Date> ps = q.getResultList();
		if (ps.size()==0)
		{
			return null;
		}
		else
		{
			return  ps.get(0);
		}
	}
	
	
	
	/**
	 * Ajout d'une date de creation pour les utilisateurs
	 */
	private int processUtilisateur(EntityManager em) 
	{
		TypedQuery<Utilisateur> q = em.createQuery("select p from Utilisateur p ORDER BY p.id DESC",Utilisateur.class);
		List<Utilisateur> ps = q.getResultList();
		
		for (int i = 0; i < ps.size(); i++) 
		{
			Utilisateur utilisateur = ps.get(i);
			
			// on détermine la plus vielle date de contrat de cet utilisateur
			Date creation = findDateCreationUtilisateur(utilisateur,em);
			
			// On determine la date de creation de l'utilisateur ayant l'id juste plus grand (précédent)  
			Date otherUtilisateur = i==0 ? null : ps.get(i-1).dateCreation;
			
			// On determine le min de ces 2 dates 
			Date ref = min(creation,otherUtilisateur);
			
			// Si elle existe, on l'utilise
			if (ref!=null)
			{
				utilisateur.dateCreation = ref;
			}
		}
				
		return ps.size();
	}



	private Date findDateCreationUtilisateur(Utilisateur utilisateur, EntityManager em) 
	{
		TypedQuery<Date> q = em.createQuery("select p.dateCreation from Contrat p where p.utilisateur=:u ORDER BY p.dateCreation",Date.class);
		q.setParameter("u", utilisateur);
		List<Date> ps = q.getResultList();
		if (ps.size()==0)
		{
			return null;
		}
		else
		{
			return  ps.get(0);
		}
	}
	
	
	// Methodes utilitaires 
	
	private Date min(Date d1, Date d2) 
	{
		if (d1==null && d2==null)
		{
			return null;
		}
		
		if (d1==null)
		{
			return d2;
		}
		
		if (d2==null)
		{
			return d1;
		}
		
		if (d1.before(d2)) 
		{
			return d1;
		}
		else
		{
			return d2;
		}
	}

	
	
}
