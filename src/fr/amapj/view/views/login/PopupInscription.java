package fr.amapj.view.views.login;


/*
 *  Copyright 2013-2016 Emmanuel BRUN (contact@amapj.fr)
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

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.view.engine.notification.NotificationHelper;
import fr.amapj.view.engine.popup.formpopup.FormPopup;

/**
 * Popup pour la saisie d'une nouvelle inscription
 *  
 */

public class PopupInscription extends FormPopup {
	
	/**
	 * 
	 */
	public PopupInscription()
	{
		popupTitle = "Inscription";
		saveButtonTitle = "Confirmer";
	}
	
	
	protected void addFields()
	{
		// Contruction de l'item
		item.addItemProperty("nom",new ObjectProperty<String>(""));
		item.addItemProperty("nom_paiement", new ObjectProperty<String>(""));
		item.addItemProperty("prenom", new ObjectProperty<String>(""));
		item.addItemProperty("email", new ObjectProperty<String>(""));
		item.addItemProperty("mobile", new ObjectProperty<String>(""));
		item.addItemProperty("fixe", new ObjectProperty<String>(""));
		item.addItemProperty("adresse", new ObjectProperty<String>(""));
		item.addItemProperty("ville", new ObjectProperty<String>(""));
		

		
		
		// Construction des champs
		addLabel("Veuillez remplir les champs suivant relatifs à votre inscription<br/><br/>" ,ContentMode.HTML);
		
		addTextField("Nom*", "nom");
		addTextField("Nom chèque*", "nom_paiement");
		addTextField("Prénom*", "prenom");
		addTextField("E-mail*", "email");
		addTextField("Mobile*", "mobile");
		addTextField("Fixe", "fixe");
		addTextField("Adresse", "adrese");
		addTextField("Ville*", "ville");
		
		//addLabel("* : champs obligatoires", ContentMode.HTML);

	}




	protected void performSauvegarder()
	{
		/*String email = (String) item.getItemProperty("email").getValue();
		String msg = new PasswordManager().sendMailForResetPassword(email);
		if (msg==null)
		{
			Notification.show("Un mail vient de vous être envoyé. Merci de vérifier votre boîte mail", Type.WARNING_MESSAGE);
		}
		else
		{
			NotificationHelper.displayNotification(msg);
		}*/
	}

}
