package fr.amapj.model.models.contrat.reel;

import fr.amapj.model.models.contrat.modele.ModeleContratDatePaiement;
import fr.amapj.model.models.remise.RemiseProducteur;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.685+0100")
@StaticMetamodel(Paiement.class)
public class Paiement_ {
	public static volatile SingularAttribute<Paiement, Long> id;
	public static volatile SingularAttribute<Paiement, Contrat> contrat;
	public static volatile SingularAttribute<Paiement, ModeleContratDatePaiement> modeleContratDatePaiement;
	public static volatile SingularAttribute<Paiement, Integer> montant;
	public static volatile SingularAttribute<Paiement, EtatPaiement> etat;
	public static volatile SingularAttribute<Paiement, RemiseProducteur> remise;
	public static volatile SingularAttribute<Paiement, String> commentaire1;
	public static volatile SingularAttribute<Paiement, String> commentaire2;
}
