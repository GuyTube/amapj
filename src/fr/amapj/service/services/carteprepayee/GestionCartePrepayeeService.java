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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.common.DateUtils;
import fr.amapj.common.SQLUtils;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.contrat.modele.EtatModeleContrat;
import fr.amapj.model.models.contrat.modele.GestionPaiement;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.contrat.modele.ModeleContratDatePaiement;
import fr.amapj.model.models.contrat.modele.ModeleContratExclude;
import fr.amapj.model.models.contrat.modele.ModeleContratProduit;
import fr.amapj.model.models.contrat.modele.NatureContrat;
import fr.amapj.model.models.contrat.reel.ContratCell;
import fr.amapj.model.models.cotisation.EtatPaiementAdhesion;
import fr.amapj.model.models.cotisation.PeriodeCotisation;
import fr.amapj.model.models.cotisation.PeriodeCotisationUtilisateur;
import fr.amapj.model.models.fichierbase.CartePrepayee;
import fr.amapj.model.models.fichierbase.EtatCartePrepayee;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.model.models.param.ChoixOuiNon;
import fr.amapj.service.engine.tools.DbToDto;
import fr.amapj.service.engine.tools.DtoToDb;
import fr.amapj.service.engine.tools.DtoToDb.ElementToAdd;
import fr.amapj.service.engine.tools.DtoToDb.ElementToUpdate;
import fr.amapj.service.engine.tools.DtoToDb.ListDiff;
import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.service.services.gestioncontratsigne.update.GestionContratSigneUpdateService;
import fr.amapj.service.services.gestioncotisation.PeriodeCotisationUtilisateurDTO;
import fr.amapj.service.services.mescontrats.ContratColDTO;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.ContratLigDTO;
import fr.amapj.service.services.notification.DeleteNotificationService;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.views.gestioncontrat.editorpart.FrequenceLivraison;

/**
 * Permet la gestion des cartes prépayées
 * 
 *  
 * 
 */
public class GestionCartePrepayeeService
{
	private final static Logger logger = LogManager.getLogger();

	public CartePrepayeeItemDTO createCartePrepayeeItem(EntityManager em, CartePrepayee cpp)
	{
		CartePrepayeeItemDTO item = new CartePrepayeeItemDTO();

		
		item.id = cpp.getId();
		item.debutValidite = cpp.getDebutValidite();
		item.finValidite = cpp.getFinValidite();
		item.description = cpp.getDescription();
		item.montant = cpp.getMontant();
		item.idPublic = cpp.getPublicId();
		item.nomUtilisateur = cpp.getUtilisateur().getNom();
		item.prenomUtilisateur = cpp.getUtilisateur().getPrenom();
		if( cpp.datePaiement != null )
			item.paiement = ChoixOuiNon.OUI;
		else {
			item.paiement = ChoixOuiNon.NON;
		}
		item.setNomProducteur(cpp.getProducteur().nom);
		return item;
	}
	
	/**
	 * Permet l'ajout ou la mise à jour d'une carte prépayée
	 * @param dto
	 */
	@DbWrite
	public void creerCarte(boolean create, CartePrepayeeItemDTO dto)
	{
		EntityManager em = TransactionHelper.getEm();
		
		CartePrepayee cpp;
		if (create==false)
		{

			cpp = em.find(CartePrepayee.class, dto.id); 
		}
		else
		{

			cpp = new CartePrepayee();
			cpp.setDateCreation(DateUtils.getDate());
			cpp.setDebutValidite(dto.getDebutValidite());
			cpp.setFinValidite(dto.getFinValidite());
			cpp.setDescription(dto.getDescription());
			cpp.setPublicId(dto.getIdPublic());
			cpp.setProducteur(em.find(Producteur.class, dto.idProducteur));
			cpp.setUtilisateur(em.find(Utilisateur.class, dto.idUtilisateur));
			
		}
		cpp.setMontant(dto.getMontant());
		cpp.setDescription(dto.getDescription());
		
		if (create==true)
		{

			em.persist(cpp);
		}	
	}

	/**
	 * Permet la suppression d'une carte prépayée
	 * @param dto
	 */
	@DbWrite
	public void supprimerCarte(Long id) throws UnableToSuppressException {

		EntityManager em = TransactionHelper.getEm();

		CartePrepayee cpp  = em.find(CartePrepayee.class, id);

		if( cpp.getDatePaiement() != null )
			throw new UnableToSuppressException("Impossible de supprimer une carte qui a été payée");

		em.remove(cpp);
	}
	
	/**
	 * Permet la suppression d'une carte prépayée
	 * @param dto
	 */
	@DbWrite
	public void BasculerPaiementCarte(Long id) {

		EntityManager em = TransactionHelper.getEm();

		CartePrepayee cpp  = em.find(CartePrepayee.class, id);
		Calendar now = Calendar.getInstance();
		
		if( cpp.getDatePaiement() == null )
			cpp.setDatePaiement(now.getTime());
		else
			cpp.setDatePaiement(null);
	}
	
	@DbRead
	public Integer getSommeToutesCartePP(Long idProducteur, Long idUtilisateur) {
		EntityManager em = TransactionHelper.getEm();
		Utilisateur util = em.find(Utilisateur.class, idUtilisateur);
		Producteur producteur = em.find(Producteur.class, idProducteur);
		Query q = em.createQuery("select SUM(c.montant) from CartePrepayee c WHERE c.utilisateur=:u"
				+ " and c.producteur=:p and c.etat=:e");
		q.setParameter("u",util);
		q.setParameter("p",producteur);
		q.setParameter("e", EtatCartePrepayee.ACTIF);
		return SQLUtils.toInt(q.getSingleResult());
	}
	
	@DbRead
	public Integer getSoldeToutesCartePP(Long idProducteur, Long idUtilisateur, Date dateDebutValidite, Date dateFinValidite) {
		EntityManager em = TransactionHelper.getEm();
		Utilisateur util = em.find(Utilisateur.class, idUtilisateur);
		Producteur producteur = em.find(Producteur.class, idProducteur);
		Query q2 = em.createQuery("select SUM(cc.qte*cc.modeleContratProduit.prix) from ContratCell cc  "
				+ " WHERE  "
				+ " cc.contrat.modeleContrat.producteur=:p "
				+ " and cc.contrat.utilisateur=:u "
				+ " and cc.contrat.modeleContrat.gestionPaiement=:gp "
				+ " and cc.modeleContratDate.dateLiv >=:d1 and cc.modeleContratDate.dateLiv <=:d2"
				);
		q2.setParameter("u",util);
		q2.setParameter("p",producteur);
		q2.setParameter("gp", GestionPaiement.CARTE_PREPAYEE);
		q2.setParameter("d1", DateUtils.suppressTime(dateDebutValidite));
		q2.setParameter("d2", DateUtils.suppressTime(dateFinValidite));
		return SQLUtils.toInt(q2.getSingleResult());
	}
}
