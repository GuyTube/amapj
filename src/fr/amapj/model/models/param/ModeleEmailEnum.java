/*
 *  Copyright 2013-2018 Emmanuel BRUN (contact@amapj.fr)
 * 
 *  This file is part of AmapJ.
 *  
 *  AmapJ is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  AmapJ is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with AmapJ.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
 package fr.amapj.model.models.param;

 import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import fr.amapj.common.Dictionary;
import fr.amapj.common.DictionaryEnum;

public enum ModeleEmailEnum
{

	// Notification de rappel du contenu des paniers
	NOTIFICATION_PANIER( DictionaryEnum.DATE_LIVRAISON, DictionaryEnum.PRODUITS_LIVRES) ,
	
	// Notification de manque de permanents
	NOTIFICATION_PERMANENCE( DictionaryEnum.DATE_PERMANENCE, DictionaryEnum.PERSONNES),
	
	// Notification périodique
	NOTIFICATION_PERIODIQUE();
	
	
	private List<DictionaryEnum> wordList;
	
	ModeleEmailEnum(DictionaryEnum... wordList) {
		this.wordList = new ArrayList<DictionaryEnum>();
		this.wordList.addAll(Arrays.asList(wordList));
		this.wordList.addAll(Dictionary.getDefaultDictionary());
	}
	
	public List<DictionaryEnum> getWordList() {
		return this.wordList;
	}
}