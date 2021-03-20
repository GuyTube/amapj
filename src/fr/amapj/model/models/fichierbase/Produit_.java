package fr.amapj.model.models.fichierbase;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-02T14:06:48.200+0100")
@StaticMetamodel(Produit.class)
public class Produit_ {
	public static volatile SingularAttribute<Produit, Long> id;
	public static volatile SingularAttribute<Produit, String> nom;
	public static volatile SingularAttribute<Produit, String> conditionnement;
	public static volatile SingularAttribute<Produit, Producteur> producteur;
	public static volatile SingularAttribute<Produit, TypFacturation> typFacturation;
}
