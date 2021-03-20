package fr.amapj.model.models.fichierbase;

import fr.amapj.model.models.param.ModeleEmailEnum;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-03-07T23:32:55.606+0100")
@StaticMetamodel(ModeleEmail.class)
public class ModeleEmail_ {
	public static volatile SingularAttribute<ModeleEmail, Long> id;
	public static volatile SingularAttribute<ModeleEmail, String> titre;
	public static volatile SingularAttribute<ModeleEmail, ModeleEmailEnum> designation;
	public static volatile SingularAttribute<ModeleEmail, String> contenu;
}
