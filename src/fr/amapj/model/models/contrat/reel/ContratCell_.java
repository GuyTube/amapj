package fr.amapj.model.models.contrat.reel;

import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.contrat.modele.ModeleContratProduit;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.684+0100")
@StaticMetamodel(ContratCell.class)
public class ContratCell_ {
	public static volatile SingularAttribute<ContratCell, Long> id;
	public static volatile SingularAttribute<ContratCell, Contrat> contrat;
	public static volatile SingularAttribute<ContratCell, ModeleContratProduit> modeleContratProduit;
	public static volatile SingularAttribute<ContratCell, ModeleContratDate> modeleContratDate;
	public static volatile SingularAttribute<ContratCell, Integer> qte;
}
