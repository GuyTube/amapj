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
 package fr.amapj.service.services.mailer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.common.LongUtils;
import fr.amapj.common.SQLUtils;
import fr.amapj.model.engine.IdentifiableUtil;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.acces.RoleList;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.reel.ContratCell;
import fr.amapj.model.models.contrat.reel.EtatPaiement;
import fr.amapj.model.models.editionspe.EditionSpecifique;
import fr.amapj.model.models.fichierbase.EtatNotification;
import fr.amapj.model.models.fichierbase.ExpediteurAmap;
import fr.amapj.model.models.fichierbase.HistoriqueEmail;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.ProducteurReferent;
import fr.amapj.model.models.fichierbase.ProducteurUtilisateur;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.model.models.param.ChoixOuiNon;
import fr.amapj.model.models.permanence.periode.PeriodePermanenceDate;
import fr.amapj.service.engine.tools.DbToDto;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.service.services.meslivraisons.MesLivraisonsDTO;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;

/**
 * Permet la gestion de l'historique des mails envoyés
 * 
 */
public class HistoriqueEmailService
{
	
	
	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES MAILS ENVOYES

	
	public HistoriqueEmailDTO createHistoriqueEmailDto(EntityManager em, HistoriqueEmail hm)
	{
		HistoriqueEmailDTO dto = new HistoriqueEmailDTO();
		dto.id = hm.getId();
		dto.dateEnvoi = hm.getDateHeureEnvoi();
		dto.role = hm.getRole()+" ("+hm.getUserExpediteur()+")";
		dto.sujet = hm.getSujet();
		dto.expediteur = (hm.getExpediteurAmap() != null) ? hm.getExpediteurAmap().getDesignation():"";
		dto.expediteurId = (hm.getExpediteurAmap() != null) ? hm.getExpediteurAmap().getId():null;
		dto.contenu = hm.getContenu();
		dto.adresseExpediteur = hm.getAdresseExpediteur();
		dto.mixExpediteur = (hm.getExpediteurAmap() != null) ? hm.getExpediteurAmap().getDesignation() : hm.getAdresseExpediteur() != null ? hm.getAdresseExpediteur()  : "";
		dto.errorMails = hm.getErrorMails();
		dto.hasEmailErreur = (hm.getErrorMails() != null && hm.getErrorMails().trim().length()>0) ? "X" : "";
		return dto;
	}
	
	
	/**
	 * Permet de charger la liste de tous les livraisons
	 * dans une transaction en lecture
	 */
	@DbRead
	public List<ExpediteurAmap> getExpediteurAmap(List<RoleList> roles)
	{
		StringBuffer roleList = new StringBuffer();
		for( RoleList role : roles ) {
			if( roleList.length()!=0 )
				roleList.append(",");
			roleList.append("\""+role.name()+"\"");
		}
		EntityManager em = TransactionHelper.getEm();

		Query q = em.createQuery("select ea from ExpediteurAmap ea where ea.role in ("+roleList+")");
		if( q.getResultList() != null )
			return (List<ExpediteurAmap>)q.getResultList();
		else
			return null;
	}
	
	@DbRead
	public static boolean isProducteur(Long userId) {
		EntityManager em = TransactionHelper.getEm();
		Query q = em.createQuery("select  r.id from ProducteurUtilisateur r  WHERE r.utilisateur.id=:userId");
		q.setParameter("userId", userId);
		
		return q.getResultList().size()>=1;

	} 

	@DbRead
	public static Producteur getProducteur(Long userId) {
		EntityManager em = TransactionHelper.getEm();
		Query q = em.createQuery("select p from Producteur p, ProducteurUtilisateur r  WHERE r.utilisateur.id=:userId"
				+" and p.id  = r.producteur.id");
		q.setParameter("userId", userId);
		
		if(q.getResultList().size() > 0 )
			return (Producteur)q.getResultList().get(0);
		else
			return null;

	}

}
