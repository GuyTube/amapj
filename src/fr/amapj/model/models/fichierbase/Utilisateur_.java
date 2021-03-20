package fr.amapj.model.models.fichierbase;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.699+0100")
@StaticMetamodel(Utilisateur.class)
public class Utilisateur_ {
	public static volatile SingularAttribute<Utilisateur, Long> id;
	public static volatile SingularAttribute<Utilisateur, String> prenom;
	public static volatile SingularAttribute<Utilisateur, String> nom;
	public static volatile SingularAttribute<Utilisateur, String> email;
	public static volatile SingularAttribute<Utilisateur, String> emailConjoint;
	public static volatile SingularAttribute<Utilisateur, String> password;
	public static volatile SingularAttribute<Utilisateur, String> salt;
	public static volatile SingularAttribute<Utilisateur, String> resetPasswordSalt;
	public static volatile SingularAttribute<Utilisateur, Date> resetPasswordDate;
	public static volatile SingularAttribute<Utilisateur, EtatUtilisateur> etatUtilisateur;
	public static volatile SingularAttribute<Utilisateur, String> numTel1;
	public static volatile SingularAttribute<Utilisateur, String> numTel2;
	public static volatile SingularAttribute<Utilisateur, String> libAdr1;
	public static volatile SingularAttribute<Utilisateur, String> codePostal;
	public static volatile SingularAttribute<Utilisateur, String> ville;
}
