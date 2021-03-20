package fr.amapj.model.models.permanence.periode;

import fr.amapj.model.models.param.ChoixOuiNon;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-29T00:33:21.124+0100")
@StaticMetamodel(PeriodePermanence.class)
public class PeriodePermanence_ {
	public static volatile SingularAttribute<PeriodePermanence, Long> id;
	public static volatile SingularAttribute<PeriodePermanence, String> nom;
	public static volatile SingularAttribute<PeriodePermanence, String> description;
	public static volatile SingularAttribute<PeriodePermanence, EtatPeriodePermanence> etat;
	public static volatile SingularAttribute<PeriodePermanence, NaturePeriodePermanence> nature;
	public static volatile SingularAttribute<PeriodePermanence, Date> dateFinInscription;
	public static volatile SingularAttribute<PeriodePermanence, Integer> flottantDelai;
	public static volatile SingularAttribute<PeriodePermanence, ChoixOuiNon> limitNbPermanenceUtil;
	public static volatile SingularAttribute<PeriodePermanence, RegleInscriptionPeriodePermanence> regleInscription;
}
