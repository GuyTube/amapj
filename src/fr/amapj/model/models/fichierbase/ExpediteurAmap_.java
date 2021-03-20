package fr.amapj.model.models.fichierbase;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-10T16:04:33.070+0100")
@StaticMetamodel(ExpediteurAmap.class)
public class ExpediteurAmap_ {
	public static volatile SingularAttribute<ExpediteurAmap, Long> id;
	public static volatile SingularAttribute<ExpediteurAmap, String> designation;
	public static volatile SingularAttribute<ExpediteurAmap, String> email;
	public static volatile SingularAttribute<ExpediteurAmap, String> emailRetour;
	public static volatile SingularAttribute<ExpediteurAmap, String> role;
}
