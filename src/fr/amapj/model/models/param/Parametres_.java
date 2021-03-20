package fr.amapj.model.models.param;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2020-02-21T23:21:02.109+0100")
@StaticMetamodel(Parametres.class)
public class Parametres_ {
	public static volatile SingularAttribute<Parametres, Long> id;
	public static volatile SingularAttribute<Parametres, String> nomAmap;
	public static volatile SingularAttribute<Parametres, String> villeAmap;
	public static volatile SingularAttribute<Parametres, String> lieuLivraison;
	public static volatile SingularAttribute<Parametres, String> heureLivraison;
	public static volatile SingularAttribute<Parametres, String> sendingMailUsername;
	public static volatile SingularAttribute<Parametres, String> sendingMailPassword;
	public static volatile SingularAttribute<Parametres, Integer> sendingMailNbMax;
	public static volatile SingularAttribute<Parametres, String> mailCopyTo;
	public static volatile SingularAttribute<Parametres, SmtpType> smtpType;
	public static volatile SingularAttribute<Parametres, String> url;
	public static volatile SingularAttribute<Parametres, String> backupReceiver;
	public static volatile SingularAttribute<Parametres, EtatModule> etatPlanningDistribution;
	public static volatile SingularAttribute<Parametres, EtatModule> etatGestionCotisation;
	public static volatile SingularAttribute<Parametres, ChoixOuiNon> envoiMailRappelPermanence;
	public static volatile SingularAttribute<Parametres, Integer> delaiMailRappelPermanence;
	public static volatile SingularAttribute<Parametres, String> titreMailRappelPermanence;
	public static volatile SingularAttribute<Parametres, String> contenuMailRappelPermanence;
	public static volatile SingularAttribute<Parametres, ChoixOuiNon> envoiMailPeriodique;
	public static volatile SingularAttribute<Parametres, Integer> numJourDansMois;
	public static volatile SingularAttribute<Parametres, String> titreMailPeriodique;
	public static volatile SingularAttribute<Parametres, String> contenuMailPeriodique;
}
