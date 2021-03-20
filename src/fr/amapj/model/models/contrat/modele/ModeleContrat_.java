package fr.amapj.model.models.contrat.modele;

import fr.amapj.model.models.extendedparam.ExtendedParam;
import fr.amapj.model.models.fichierbase.Producteur;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-03-13T00:07:04.370+0100")
@StaticMetamodel(ModeleContrat.class)
public class ModeleContrat_ {
	public static volatile SingularAttribute<ModeleContrat, Long> id;
	public static volatile SingularAttribute<ModeleContrat, String> nom;
	public static volatile SingularAttribute<ModeleContrat, String> description;
	public static volatile SingularAttribute<ModeleContrat, Producteur> producteur;
	public static volatile SingularAttribute<ModeleContrat, EtatModeleContrat> etat;
	public static volatile SingularAttribute<ModeleContrat, Date> dateFinInscription;
	public static volatile SingularAttribute<ModeleContrat, Integer> nbMaxSouscription;
	public static volatile SingularAttribute<ModeleContrat, GestionPaiement> gestionPaiement;
	public static volatile SingularAttribute<ModeleContrat, String> textPaiement;
	public static volatile SingularAttribute<ModeleContrat, String> libCheque;
	public static volatile SingularAttribute<ModeleContrat, Date> dateRemiseCheque;
	public static volatile SingularAttribute<ModeleContrat, NatureContrat> nature;
	public static volatile SingularAttribute<ModeleContrat, Integer> cartePrepayeeDelai;
	public static volatile SingularAttribute<ModeleContrat, ExtendedParam> miseEnFormeGraphique;
	public static volatile SingularAttribute<ModeleContrat, Integer> jokerNbMin;
	public static volatile SingularAttribute<ModeleContrat, Integer> jokerNbMax;
	public static volatile SingularAttribute<ModeleContrat, JokerMode> jokerMode;
	public static volatile SingularAttribute<ModeleContrat, Integer> jokerDelai;
}
