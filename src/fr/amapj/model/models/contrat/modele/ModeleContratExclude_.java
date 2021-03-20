package fr.amapj.model.models.contrat.modele;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.681+0100")
@StaticMetamodel(ModeleContratExclude.class)
public class ModeleContratExclude_ {
	public static volatile SingularAttribute<ModeleContratExclude, Long> id;
	public static volatile SingularAttribute<ModeleContratExclude, ModeleContrat> modeleContrat;
	public static volatile SingularAttribute<ModeleContratExclude, ModeleContratProduit> produit;
	public static volatile SingularAttribute<ModeleContratExclude, ModeleContratDate> date;
}
