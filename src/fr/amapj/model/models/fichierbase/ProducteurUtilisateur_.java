package fr.amapj.model.models.fichierbase;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.694+0100")
@StaticMetamodel(ProducteurUtilisateur.class)
public class ProducteurUtilisateur_ {
	public static volatile SingularAttribute<ProducteurUtilisateur, Long> id;
	public static volatile SingularAttribute<ProducteurUtilisateur, Producteur> producteur;
	public static volatile SingularAttribute<ProducteurUtilisateur, Utilisateur> utilisateur;
	public static volatile SingularAttribute<ProducteurUtilisateur, EtatNotification> notification;
	public static volatile SingularAttribute<ProducteurUtilisateur, Integer> indx;
}
