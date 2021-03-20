package fr.amapj.model.models.stats;

import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.fichierbase.Utilisateur;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-01T17:51:28.711+0100")
@StaticMetamodel(NotificationDone.class)
public class NotificationDone_ {
	public static volatile SingularAttribute<NotificationDone, Long> id;
	public static volatile SingularAttribute<NotificationDone, TypNotificationDone> typNotificationDone;
	public static volatile SingularAttribute<NotificationDone, ModeleContratDate> modeleContratDate;
	public static volatile SingularAttribute<NotificationDone, Date> dateMailPeriodique;
	public static volatile SingularAttribute<NotificationDone, Utilisateur> utilisateur;
	public static volatile SingularAttribute<NotificationDone, Date> dateEnvoi;
}
