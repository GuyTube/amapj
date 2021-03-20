package fr.amapj.model.models.fichierbase;

import fr.amapj.model.models.editionspe.EditionSpecifique;
import fr.amapj.model.models.param.ChoixOuiNon;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.692+0100")
@StaticMetamodel(Producteur.class)
public class Producteur_ {
	public static volatile SingularAttribute<Producteur, Long> id;
	public static volatile SingularAttribute<Producteur, String> nom;
	public static volatile SingularAttribute<Producteur, String> description;
	public static volatile SingularAttribute<Producteur, String> emailContact;
	public static volatile SingularAttribute<Producteur, ChoixOuiNon> feuilleDistributionGrille;
	public static volatile SingularAttribute<Producteur, ChoixOuiNon> feuilleDistributionListe;
	public static volatile SingularAttribute<Producteur, EditionSpecifique> etiquette;
	public static volatile SingularAttribute<Producteur, EditionSpecifique> engagement;
	public static volatile SingularAttribute<Producteur, String> libContrat;
	public static volatile SingularAttribute<Producteur, Integer> delaiModifContrat;
}
