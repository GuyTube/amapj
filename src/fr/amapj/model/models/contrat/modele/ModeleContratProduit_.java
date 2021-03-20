package fr.amapj.model.models.contrat.modele;

import fr.amapj.model.models.fichierbase.Produit;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-03-08T14:58:12.509+0100")
@StaticMetamodel(ModeleContratProduit.class)
public class ModeleContratProduit_ {
	public static volatile SingularAttribute<ModeleContratProduit, Long> id;
	public static volatile SingularAttribute<ModeleContratProduit, ModeleContrat> modeleContrat;
	public static volatile SingularAttribute<ModeleContratProduit, Produit> produit;
	public static volatile SingularAttribute<ModeleContratProduit, Integer> indx;
	public static volatile SingularAttribute<ModeleContratProduit, Integer> prix;
	public static volatile SingularAttribute<ModeleContratProduit, Integer> nbMaxParLivraison;
}
