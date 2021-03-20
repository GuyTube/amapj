package fr.amapj.model.models.fichierbase;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-15T00:02:35.930+0100")
@StaticMetamodel(HistoriqueEmail.class)
public class HistoriqueEmail_ {
	public static volatile SingularAttribute<HistoriqueEmail, Long> id;
	public static volatile SingularAttribute<HistoriqueEmail, ExpediteurAmap> expediteurAmap;
	public static volatile SingularAttribute<HistoriqueEmail, String> roleExpediteur;
	public static volatile SingularAttribute<HistoriqueEmail, String> sujet;
	public static volatile SingularAttribute<HistoriqueEmail, String> contenu;
	public static volatile SingularAttribute<HistoriqueEmail, String> userExpediteur;
	public static volatile SingularAttribute<HistoriqueEmail, Date> dateHeureEnvoi;
	public static volatile SingularAttribute<HistoriqueEmail, String> errorMails;
	public static volatile SingularAttribute<HistoriqueEmail, String> adresseExpediteur;
}
