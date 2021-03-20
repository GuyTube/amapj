package fr.amapj.model.models.editionspe;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.689+0100")
@StaticMetamodel(EditionSpecifique.class)
public class EditionSpecifique_ {
	public static volatile SingularAttribute<EditionSpecifique, Long> id;
	public static volatile SingularAttribute<EditionSpecifique, String> nom;
	public static volatile SingularAttribute<EditionSpecifique, TypEditionSpecifique> typEditionSpecifique;
	public static volatile SingularAttribute<EditionSpecifique, String> content;
}
