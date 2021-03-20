package fr.amapj.service.services.utilisateur;

public enum PreferenceUtilisateurEnum {
	PREF_MAIL_CONJOINT("MAIL_CONJOINT",1), 
	PREF_DELAI_NOTIF_DISTRI("DELAI_NOTIF_DISTRI",1);
	
	private String nom;
	private int maxValues;
	
	PreferenceUtilisateurEnum(String nom, int max) {
		this.nom = nom;
		this.maxValues = max;
	}
	
	static public PreferenceUtilisateurEnum getPrefByNom(String nom) {
		for( PreferenceUtilisateurEnum pref : PreferenceUtilisateurEnum.values() ) {
			if(pref.getNom().equals(nom) ) {
				return pref;
			}
		}
		return null;
	}
	
	public String getNom() {
		return this.nom;
	}
	
	public int getMax() {
		return this.maxValues;
	}
}
