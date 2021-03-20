package fr.amapj.model.models.saas;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.710+0100")
@StaticMetamodel(LogAccess.class)
public class LogAccess_ {
	public static volatile SingularAttribute<LogAccess, Long> id;
	public static volatile SingularAttribute<LogAccess, String> ip;
	public static volatile SingularAttribute<LogAccess, String> browser;
	public static volatile SingularAttribute<LogAccess, String> nom;
	public static volatile SingularAttribute<LogAccess, String> prenom;
	public static volatile SingularAttribute<LogAccess, Long> idUtilisateur;
	public static volatile SingularAttribute<LogAccess, Date> dateIn;
	public static volatile SingularAttribute<LogAccess, Date> dateOut;
	public static volatile SingularAttribute<LogAccess, Integer> activityTime;
	public static volatile SingularAttribute<LogAccess, String> dbName;
	public static volatile SingularAttribute<LogAccess, String> logFileName;
	public static volatile SingularAttribute<LogAccess, TypLog> typLog;
	public static volatile SingularAttribute<LogAccess, Integer> nbError;
	public static volatile SingularAttribute<LogAccess, Integer> sudo;
}
