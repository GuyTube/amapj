package fr.amapj.model.models.cotisation;

import fr.amapj.model.models.fichierbase.Utilisateur;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.688+0100")
@StaticMetamodel(PeriodeCotisationUtilisateur.class)
public class PeriodeCotisationUtilisateur_ {
	public static volatile SingularAttribute<PeriodeCotisationUtilisateur, Long> id;
	public static volatile SingularAttribute<PeriodeCotisationUtilisateur, PeriodeCotisation> periodeCotisation;
	public static volatile SingularAttribute<PeriodeCotisationUtilisateur, Utilisateur> utilisateur;
	public static volatile SingularAttribute<PeriodeCotisationUtilisateur, Date> dateAdhesion;
	public static volatile SingularAttribute<PeriodeCotisationUtilisateur, Date> dateReceptionCheque;
	public static volatile SingularAttribute<PeriodeCotisationUtilisateur, Integer> montantAdhesion;
	public static volatile SingularAttribute<PeriodeCotisationUtilisateur, EtatPaiementAdhesion> etatPaiementAdhesion;
	public static volatile SingularAttribute<PeriodeCotisationUtilisateur, TypePaiementAdhesion> typePaiementAdhesion;
}
