package fr.amapj.model.models.fichierbase;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-26T23:37:26.619+0100")
@StaticMetamodel(PreferenceUtilisateur.class)
public class PreferenceUtilisateur_ {
	public static volatile SingularAttribute<PreferenceUtilisateur, Long> id;
	public static volatile SingularAttribute<PreferenceUtilisateur, Utilisateur> utilisateur;
	public static volatile SingularAttribute<PreferenceUtilisateur, String> nomPref;
	public static volatile SingularAttribute<PreferenceUtilisateur, String> valeurPref;
}
