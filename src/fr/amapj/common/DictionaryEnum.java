package fr.amapj.common;

public enum DictionaryEnum {
	LIEU_LIVRAISON, NOM_AMAP, HEURE_LIVRAISON, LINK, 
	DATE_LIVRAISON, NOM, PRENOM, PERMANENCE, PRODUITS_LIVRES,
	DATE_PERMANENCE, PERSONNES, EMAIL, EMAILCONJOINT, NUMTEL1, 
	NUMTEL2, VILLE, ADRESSE;	


	public String getReplaceString() {
		return "#"+this.name()+"#";
	}
}
