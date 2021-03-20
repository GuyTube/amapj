package fr.amapj.model.models.cotisation;

import fr.amapj.model.models.editionspe.EditionSpecifique;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.686+0100")
@StaticMetamodel(PeriodeCotisation.class)
public class PeriodeCotisation_ {
	public static volatile SingularAttribute<PeriodeCotisation, Long> id;
	public static volatile SingularAttribute<PeriodeCotisation, String> nom;
	public static volatile SingularAttribute<PeriodeCotisation, Integer> montantMini;
	public static volatile SingularAttribute<PeriodeCotisation, Integer> montantConseille;
	public static volatile SingularAttribute<PeriodeCotisation, Date> dateDebutInscription;
	public static volatile SingularAttribute<PeriodeCotisation, Date> dateFinInscription;
	public static volatile SingularAttribute<PeriodeCotisation, Date> dateDebut;
	public static volatile SingularAttribute<PeriodeCotisation, Date> dateFin;
	public static volatile SingularAttribute<PeriodeCotisation, String> textPaiement;
	public static volatile SingularAttribute<PeriodeCotisation, String> libCheque;
	public static volatile SingularAttribute<PeriodeCotisation, Date> dateRemiseCheque;
	public static volatile SingularAttribute<PeriodeCotisation, EditionSpecifique> bulletinAdhesion;
}
