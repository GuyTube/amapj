package fr.amapj.model.models.permanence.periode;

import fr.amapj.model.models.fichierbase.Utilisateur;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.704+0100")
@StaticMetamodel(PeriodePermanenceUtilisateur.class)
public class PeriodePermanenceUtilisateur_ {
	public static volatile SingularAttribute<PeriodePermanenceUtilisateur, Long> id;
	public static volatile SingularAttribute<PeriodePermanenceUtilisateur, PeriodePermanence> periodePermanence;
	public static volatile SingularAttribute<PeriodePermanenceUtilisateur, Utilisateur> utilisateur;
	public static volatile SingularAttribute<PeriodePermanenceUtilisateur, Integer> nbParticipation;
}
