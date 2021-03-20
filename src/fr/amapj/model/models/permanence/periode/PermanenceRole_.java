package fr.amapj.model.models.permanence.periode;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.705+0100")
@StaticMetamodel(PermanenceRole.class)
public class PermanenceRole_ {
	public static volatile SingularAttribute<PermanenceRole, Long> id;
	public static volatile SingularAttribute<PermanenceRole, String> nom;
	public static volatile SingularAttribute<PermanenceRole, String> description;
	public static volatile SingularAttribute<PermanenceRole, Boolean> defaultRole;
}
