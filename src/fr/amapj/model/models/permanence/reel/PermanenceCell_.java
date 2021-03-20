package fr.amapj.model.models.permanence.reel;

import fr.amapj.model.models.permanence.periode.PeriodePermanenceDate;
import fr.amapj.model.models.permanence.periode.PeriodePermanenceUtilisateur;
import fr.amapj.model.models.permanence.periode.PermanenceRole;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.706+0100")
@StaticMetamodel(PermanenceCell.class)
public class PermanenceCell_ {
	public static volatile SingularAttribute<PermanenceCell, Long> id;
	public static volatile SingularAttribute<PermanenceCell, PeriodePermanenceDate> periodePermanenceDate;
	public static volatile SingularAttribute<PermanenceCell, PeriodePermanenceUtilisateur> periodePermanenceUtilisateur;
	public static volatile SingularAttribute<PermanenceCell, PermanenceRole> permanenceRole;
	public static volatile SingularAttribute<PermanenceCell, Date> dateNotification;
	public static volatile SingularAttribute<PermanenceCell, Integer> indx;
}
