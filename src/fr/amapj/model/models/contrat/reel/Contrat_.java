package fr.amapj.model.models.contrat.reel;

import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.fichierbase.Utilisateur;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.683+0100")
@StaticMetamodel(Contrat.class)
public class Contrat_ {
	public static volatile SingularAttribute<Contrat, Long> id;
	public static volatile SingularAttribute<Contrat, ModeleContrat> modeleContrat;
	public static volatile SingularAttribute<Contrat, Utilisateur> utilisateur;
	public static volatile SingularAttribute<Contrat, Date> dateCreation;
	public static volatile SingularAttribute<Contrat, Date> dateModification;
	public static volatile SingularAttribute<Contrat, Integer> montantAvoir;
}
