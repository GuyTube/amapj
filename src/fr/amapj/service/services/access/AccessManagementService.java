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
 package fr.amapj.service.services.access;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.acces.RoleList;
import fr.amapj.model.models.fichierbase.EtatProducteur;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.RoleAdmin;
import fr.amapj.model.models.fichierbase.RoleTresorier;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.engine.tools.DbToDto;

/**
 * Permet la gestion des droits d'accès
 * 
 *  
 *
 */
public class AccessManagementService
{
	

	/**
	 * Cette méthode détermine la liste des rôles de cet utilisateur
	 * @param u
	 * @param em
	 * @return
	 */
	public List<RoleList> getUserRole(Utilisateur u, EntityManager em)
	{
		List<RoleList> res = new ArrayList<RoleList>();
		
		if (isMaster(em,u))
		{
			res.add(RoleList.MASTER);
			res.add(RoleList.ADMIN);
			res.add(RoleList.TRESORIER);
			return res;
		}
		
		if (isAdmin(em,u))
		{
			res.add(RoleList.ADMIN);
			res.add(RoleList.TRESORIER);
			res.add(RoleList.PRODUCTEUR);
			res.add(RoleList.REFERENT);
			res.add(RoleList.ADHERENT);
			return res;
		}
		
		if (isTresorier(em,u))
		{
			res.add(RoleList.TRESORIER);
			res.add(RoleList.PRODUCTEUR);
			res.add(RoleList.REFERENT);
			res.add(RoleList.ADHERENT);
			return res;
		}
		
		if (isReferent(em,u))
		{
			res.add(RoleList.REFERENT);
			res.add(RoleList.PRODUCTEUR);
			res.add(RoleList.ADHERENT);
			return res;
		}
		
		if (isProducteur(em,u))
		{
			res.add(RoleList.PRODUCTEUR);
			res.add(RoleList.ADHERENT);
			return res;
		}
		
		res.add(RoleList.ADHERENT);
		return res;
	}
	
	
	/**
	 Permet d'identifier facilement les roles d'un utilisateur
	 */
	public String getRoleAsString(EntityManager em, Utilisateur u)
	{
		if (isMaster(em,u))
		{
			return "MASTER";
		}
		
		if (isAdmin(em,u))
		{
			return "ADMIN";
		}
		
		if (isTresorier(em,u))
		{
			return "TRESORIER";
		}
		
		boolean ref = isReferent(em,u);
		boolean prod = isProducteur(em, u);
		
		if (ref && prod)
		{
			return "REFERENT et PRODUCTEUR";
		}
		
		if (ref)
		{
			return "REFERENT";
		}
		
		if (prod)
		{
			return "PRODUCTEUR";
		}
		
		return "ADHERENT";
	}
	
	
	public boolean isMaster(EntityManager em, Utilisateur u)
	{
		Query q = em.createQuery("select r.id from RoleMaster r  WHERE r.utilisateur=:u");
		q.setParameter("u", u);
		return q.getResultList().size()>=1;
	}
	

	public boolean isAdmin(EntityManager em, Utilisateur u)
	{
		Query q = em.createQuery("select r.id from RoleAdmin r  WHERE r.utilisateur=:u");
		q.setParameter("u", u);
		return q.getResultList().size()>=1;
	}



	public boolean isTresorier(EntityManager em, Utilisateur u)
	{
		Query q = em.createQuery("select r.id from RoleTresorier r  WHERE r.utilisateur=:u");
		q.setParameter("u", u);
		return q.getResultList().size()>=1;
	}



	public boolean isReferent(EntityManager em, Utilisateur u)
	{
		Query q = em.createQuery("select r.id from ProducteurReferent r  WHERE r.referent=:u");
		q.setParameter("u", u);
		return q.getResultList().size()>=1;
	}



	public boolean isProducteur(EntityManager em, Utilisateur u)
	{
		Query q = em.createQuery("select r.id from ProducteurUtilisateur r  WHERE r.utilisateur=:u");
		q.setParameter("u", u);
		return q.getResultList().size()>=1;

	}



	// PARTIE REQUETAGE POUR LES PRODUCTEURS AUTORISES

	/**
	 * Permet de charger la liste de tous les producteurs autorisés pour cet utilisateur 
	 * dans une transaction en lecture
	 * 
	 * si actifOnly = true : il y a uniquement les producteurs actifs
	 * si actifOnly = false : il y a tous les producteurs (actifs et inactifs)
	 */
	@DbRead
	public List<Producteur> getAccessLivraisonProducteur(List<RoleList> roles,Long idUtilisateur,boolean actifOnly)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Utilisateur user = em.find(Utilisateur.class, idUtilisateur);
		TypedQuery<Producteur> q;
		List<Producteur> res = new ArrayList<Producteur>();
		// TODO PMO : WARNING : ADDED ADHERENT SO THAT THEY CAN VIEW A LIST OF PRODUCTEURS TO CHOOSE FROM(VIEW ONLY IN THE SCREEN)
		// WILL BE BETTER TO SEPARATE THE COMPONENTS OR TO REFACTOR THIS SECTION
		if ( (roles.contains(RoleList.ADMIN)) ||  (roles.contains(RoleList.TRESORIER)) ||  (roles.contains(RoleList.ADHERENT)) )
		{
			// Recherche tous les producteurs
			if (actifOnly)
			{
				q = em.createQuery("select p from Producteur p WHERE p.etat = :etat order by p.nom",Producteur.class);
				q.setParameter("etat", EtatProducteur.ACTIF);
			}
			else
			{
				q = em.createQuery("select p from Producteur p order by p.nom",Producteur.class);
			}
			res.addAll(q.getResultList() );
			return res;
		}
		
		
		// Recherche en tant que producteur
		if (actifOnly)
		{
			q = em.createQuery("select distinct(c.producteur) from ProducteurUtilisateur c WHERE c.utilisateur=:u AND c.producteur.etat=:etat order by c.producteur.nom",Producteur.class);
			q.setParameter("etat", EtatProducteur.ACTIF);
			q.setParameter("u", user);
		}
		else
		{
			q = em.createQuery("select distinct(c.producteur) from ProducteurUtilisateur c WHERE c.utilisateur=:u order by c.producteur.nom",Producteur.class);
			q.setParameter("u", user);
		}
		res.addAll(q.getResultList() );
				
		// Recherche en tant que referent
		if (actifOnly)
		{
			q = em.createQuery("select distinct(c.producteur) from ProducteurReferent c WHERE c.referent=:u AND c.producteur.etat=:etat order by c.producteur.nom",Producteur.class);
			q.setParameter("etat", EtatProducteur.ACTIF);
			q.setParameter("u", user);
		}
		else
		{
			q = em.createQuery("select distinct(c.producteur) from ProducteurReferent c WHERE c.referent=:u order by c.producteur.nom",Producteur.class);
			q.setParameter("u", user);
		}
		res.addAll(q.getResultList() );				
			
		return res;

	}


	// PARTIE fichier de base
	
	/**
	 * Permet de charger la liste de tous les administrateurs
	 * dans une transaction en lecture
	 */
	@DbRead
	public List<AdminTresorierDTO> getAllAdmin()
	{
		EntityManager em = TransactionHelper.getEm();
		Query q = em.createQuery("select r from RoleAdmin r");
		return  DbToDto.transform(q, (RoleAdmin r) ->loadAdminDTO(r));
	}
	
	
	/**
	 * Permet de charger un administrateur
	 */
	private AdminTresorierDTO loadAdminDTO(RoleAdmin roleAdmin)
	{	
		
		AdminTresorierDTO dto = new AdminTresorierDTO();
		
		dto.id = roleAdmin.getId();
		dto.utilisateurId = roleAdmin.getUtilisateur().getId();
		dto.nom = roleAdmin.getUtilisateur().getNom();
		dto.prenom = roleAdmin.getUtilisateur().getPrenom();
		
		return dto;		
	}

	/**
	 * Création d'un administrateur dans la base
	 */
	@DbWrite
	public void createAdmin(AdminTresorierDTO dto)
	{
		EntityManager em = TransactionHelper.getEm();
		RoleAdmin p =  new RoleAdmin();
		p.setUtilisateur(em.find(Utilisateur.class, dto.utilisateurId));
		em.persist(p);
		
	}
	
	
	/**
	 * Suppression d'un administrateur dans la base
	 */
	@DbWrite
	public void deleteAdmin(Long id)
	{
		EntityManager em = TransactionHelper.getEm();
		RoleAdmin p =  em.find(RoleAdmin.class, id);
		em.remove(p);
	}
	
	
	/**
	 * Permet de charger la liste de tous les tresoriers
	 * dans une transaction en lecture
	 */
	@DbRead
	public List<AdminTresorierDTO> getAllTresorier()
	{
		EntityManager em = TransactionHelper.getEm();
		Query q = em.createQuery("select r from RoleTresorier r");
		return  DbToDto.transform(q, (RoleTresorier r) ->loadTresorierDTO(r));
	}
	
	
	/**
	 * Permet de charger un administrateur
	 */
	private AdminTresorierDTO loadTresorierDTO(RoleTresorier r)
	{	
		AdminTresorierDTO dto = new AdminTresorierDTO();
		
		dto.id = r.getId();
		dto.utilisateurId = r.getUtilisateur().getId();
		dto.nom = r.getUtilisateur().getNom();
		dto.prenom = r.getUtilisateur().getPrenom();
		
		return dto;		
	}

	

	/**
	 * Création d'un trésorier
	 */
	@DbWrite
	public void createTresorier(AdminTresorierDTO dto)
	{
		EntityManager em = TransactionHelper.getEm();
		RoleTresorier p  = new RoleTresorier();
		p.setUtilisateur(em.find(Utilisateur.class, dto.utilisateurId));
		em.persist(p);
	}	
	
	/**
	 * Suppression d'un trésorier dans la base
	 */
	@DbWrite
	public void deleteTresorier(Long id)
	{
		EntityManager em = TransactionHelper.getEm();
		RoleTresorier p =  em.find(RoleTresorier.class, id);
		em.remove(p);
	}

	/** 
	 * Retourne null si cet utilisateur n'est ni PRODUCTEUR ni REFERENT ni TRESORIER ni ADMIN
	 * 
	 * sinon retourne ce qui empeche la suppression de cet utilisateur 
	 * 
	 */
	@DbRead
	public String canBeDeleted(Long idUtilisateur) 
	{
		EntityManager em = TransactionHelper.getEm();
		Utilisateur u = em.find(Utilisateur.class, idUtilisateur);
		
		if (isAdmin(em, u))
		{
			return "L'utilisateur est ADMIN";
		}
		if (isTresorier(em, u))
		{
			return "L'utilisateur est TRESORIER";
		}
		
		TypedQuery<Producteur> q = em.createQuery("select r.producteur from ProducteurReferent r  WHERE r.referent=:u",Producteur.class);
		q.setParameter("u", u);
		List<Producteur> ps = q.getResultList();
		if (ps.size()>=1)
		{
			return "L'utilisateur est marqué comme REFERENT pour le producteur "+ps.get(0).nom;
		}
		
		q = em.createQuery("select r.producteur from ProducteurUtilisateur r  WHERE r.utilisateur=:u",Producteur.class);
		q.setParameter("u", u);
		ps = q.getResultList();
		if (ps.size()>=1)
		{
			return "L'utilisateur est marqué comme PRODUCTEUR pour le producteur "+ps.get(0).nom;
		}
		return null;	
	}
	
	/** 
	 * Retourne la liste détaillée des roles de cet utilisateur 
	 * 
	 */
	@DbRead
	public List<String> detailDesRoles(Long idUtilisateur) 
	{
		 List<String> res= new ArrayList<String>();
		 
		EntityManager em = TransactionHelper.getEm();
		Utilisateur u = em.find(Utilisateur.class, idUtilisateur);
		
		if (isAdmin(em, u))
		{
			res.add("Cet utilisateur est ADMIN");
		}
		if (isTresorier(em, u))
		{
			res.add("Cet utilisateur est TRESORIER");
		}
		
		TypedQuery<Producteur> q = em.createQuery("select r.producteur from ProducteurReferent r  WHERE r.referent=:u",Producteur.class);
		q.setParameter("u", u);
		for (Producteur producteur : q.getResultList()) 
		{
			res.add("Cet utilisateur est REFERENT pour le producteur "+producteur.nom);
		}
		
		
		q = em.createQuery("select r.producteur from ProducteurUtilisateur r  WHERE r.utilisateur=:u",Producteur.class);
		q.setParameter("u", u);
		for (Producteur producteur : q.getResultList()) 
		{
			res.add("Cet utilisateur est PRODUCTEUR pour le producteur "+producteur.nom);
		}
		
		return res;	
	}
	
	
	
}
