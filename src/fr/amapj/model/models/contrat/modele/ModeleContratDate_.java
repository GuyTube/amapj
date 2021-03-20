package fr.amapj.model.models.contrat.modele;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.680+0100")
@StaticMetamodel(ModeleContratDate.class)
public class ModeleContratDate_ {
	public static volatile SingularAttribute<ModeleContratDate, Long> id;
	public static volatile SingularAttribute<ModeleContratDate, ModeleContrat> modeleContrat;
	public static volatile SingularAttribute<ModeleContratDate, Date> dateLiv;
}
