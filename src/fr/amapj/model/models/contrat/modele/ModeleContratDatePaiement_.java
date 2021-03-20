package fr.amapj.model.models.contrat.modele;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.680+0100")
@StaticMetamodel(ModeleContratDatePaiement.class)
public class ModeleContratDatePaiement_ {
	public static volatile SingularAttribute<ModeleContratDatePaiement, Long> id;
	public static volatile SingularAttribute<ModeleContratDatePaiement, ModeleContrat> modeleContrat;
	public static volatile SingularAttribute<ModeleContratDatePaiement, Date> datePaiement;
}
