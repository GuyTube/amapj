package fr.amapj.model.models.remise;

import fr.amapj.model.models.contrat.modele.ModeleContratDatePaiement;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.707+0100")
@StaticMetamodel(RemiseProducteur.class)
public class RemiseProducteur_ {
	public static volatile SingularAttribute<RemiseProducteur, Long> id;
	public static volatile SingularAttribute<RemiseProducteur, Date> dateCreation;
	public static volatile SingularAttribute<RemiseProducteur, Date> dateRemise;
	public static volatile SingularAttribute<RemiseProducteur, ModeleContratDatePaiement> datePaiement;
	public static volatile SingularAttribute<RemiseProducteur, Integer> montant;
}
