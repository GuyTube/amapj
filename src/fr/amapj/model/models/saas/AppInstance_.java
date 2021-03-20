package fr.amapj.model.models.saas;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:56:51.257+0100")
@StaticMetamodel(AppInstance.class)
public class AppInstance_ {
	public static volatile SingularAttribute<AppInstance, Long> id;
	public static volatile SingularAttribute<AppInstance, String> nomInstance;
	public static volatile SingularAttribute<AppInstance, Date> dateCreation;
	public static volatile SingularAttribute<AppInstance, String> dbms;
	public static volatile SingularAttribute<AppInstance, String> dbUserName;
	public static volatile SingularAttribute<AppInstance, String> dbPassword;
}
