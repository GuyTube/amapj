package fr.amapj.model.models.fichierbase;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-05-12T21:56:34.286+0200")
@StaticMetamodel(CartePrepayee.class)
public class CartePrepayee_ {
	public static volatile SingularAttribute<CartePrepayee, Long> id;
	public static volatile SingularAttribute<CartePrepayee, String> publicId;
	public static volatile SingularAttribute<CartePrepayee, String> description;
	public static volatile SingularAttribute<CartePrepayee, Integer> montant;
	public static volatile SingularAttribute<CartePrepayee, Date> dateCreation;
	public static volatile SingularAttribute<CartePrepayee, Date> debutValidite;
	public static volatile SingularAttribute<CartePrepayee, Date> finValidite;
	public static volatile SingularAttribute<CartePrepayee, Producteur> producteur;
	public static volatile SingularAttribute<CartePrepayee, Utilisateur> utilisateur;
	public static volatile SingularAttribute<CartePrepayee, Date> datePaiement;
	public static volatile SingularAttribute<CartePrepayee, EtatCartePrepayee> etat;
}
