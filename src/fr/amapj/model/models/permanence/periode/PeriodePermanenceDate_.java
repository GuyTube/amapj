package fr.amapj.model.models.permanence.periode;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.703+0100")
@StaticMetamodel(PeriodePermanenceDate.class)
public class PeriodePermanenceDate_ {
	public static volatile SingularAttribute<PeriodePermanenceDate, Long> id;
	public static volatile SingularAttribute<PeriodePermanenceDate, PeriodePermanence> periodePermanence;
	public static volatile SingularAttribute<PeriodePermanenceDate, Date> datePerm;
	public static volatile SingularAttribute<PeriodePermanenceDate, Integer> nbPlace;
}
