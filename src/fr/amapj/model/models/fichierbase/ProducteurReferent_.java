package fr.amapj.model.models.fichierbase;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.693+0100")
@StaticMetamodel(ProducteurReferent.class)
public class ProducteurReferent_ {
	public static volatile SingularAttribute<ProducteurReferent, Long> id;
	public static volatile SingularAttribute<ProducteurReferent, Producteur> producteur;
	public static volatile SingularAttribute<ProducteurReferent, Utilisateur> referent;
	public static volatile SingularAttribute<ProducteurReferent, EtatNotification> notification;
	public static volatile SingularAttribute<ProducteurReferent, Integer> indx;
}
