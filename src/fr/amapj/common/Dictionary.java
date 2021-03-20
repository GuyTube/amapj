package fr.amapj.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;

public class Dictionary {
	private final static Logger logger = LogManager.getLogger();

	private HashMap<DictionaryEnum,String> words = new HashMap<DictionaryEnum,String>();
	
	public Dictionary() {
		this(null);
	}
	
	public Dictionary(Utilisateur u) {
		ParametresService ps = new ParametresService();
		ParametresDTO params = ps.getParametres();
		setUtilisateur(u, params);
		words.put(DictionaryEnum.LIEU_LIVRAISON, params.getLieuLivraison());
		words.put(DictionaryEnum.NOM_AMAP, params.getNomAmap());
		words.put(DictionaryEnum.HEURE_LIVRAISON, params.getHeureLivraison());
	
	}
	
	public void addWord(DictionaryEnum key, String val) {
		words.put(key, val);
	}
	
	public HashMap<DictionaryEnum,String> getWords() {
		return words;
	}
	
	public String substitue(String message) {
		String fullMessage = message;
		for(DictionaryEnum key : words.keySet()) {
			if( words.get(key) != null ) {
				fullMessage = fullMessage.replace(key.getReplaceString(), words.get(key));
			} else {
				logger.info("Pas de substitution: "+key);
			}
		}
		// Unknown keys are deleted
		//fullMessage = fullMessage.replaceAll("%[a-zA-Z_0-9]%", "");
		return fullMessage;
	}
	
	public String getVal(DictionaryEnum d ) {
		return words.get(d);
	}
	
	public void setUtilisateur(Utilisateur u, ParametresDTO params) {
		if( u != null ) {
			String link = params.getUrl();
			link = link + "?username="+u.getEmail();
			words.put(DictionaryEnum.LINK, link);	
			words.put(DictionaryEnum.NOM, u.getNom());
			words.put(DictionaryEnum.PRENOM, u.getPrenom());
			words.put(DictionaryEnum.EMAIL, u.getEmail());
			words.put(DictionaryEnum.EMAILCONJOINT, u.getEmailConjoint());
			words.put(DictionaryEnum.NUMTEL1, u.getNumTel1());
			words.put(DictionaryEnum.NUMTEL2, u.getNumTel2());
			words.put(DictionaryEnum.VILLE, u.getVille());
			words.put(DictionaryEnum.ADRESSE, u.getLibAdr1());
		}
	}
	
	public static List<DictionaryEnum> getDefaultDictionary() {
		ArrayList<DictionaryEnum> def = new ArrayList<DictionaryEnum>();
		def.add(DictionaryEnum.LIEU_LIVRAISON);
		def.add(DictionaryEnum.NOM_AMAP);
		def.add(DictionaryEnum.HEURE_LIVRAISON);
		def.add(DictionaryEnum.LINK);
		def.add(DictionaryEnum.NOM);
		def.add(DictionaryEnum.PRENOM);
		def.add(DictionaryEnum.EMAIL);
		def.add(DictionaryEnum.EMAILCONJOINT);
		def.add(DictionaryEnum.NUMTEL1);
		def.add(DictionaryEnum.NUMTEL2);
		def.add(DictionaryEnum.VILLE);
		def.add(DictionaryEnum.ADRESSE);
		return def;
	}

}
