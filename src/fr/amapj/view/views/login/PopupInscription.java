package fr.amapj.view.views.login;

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
@SuppressWarnings("serial")
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
		item.addItemProperty("password", new ObjectProperty<String>(""));
		

		
		
		// Construction des champs
		addLabel("Veuillez remplir les champs suivant relatifs à votre inscription<br/><br/>" ,ContentMode.HTML);
		
		addTextField("Nom*", "nom");
		addTextField("Nom chèque*", "nom_paiement");
		addTextField("Prénom*", "prenom");
		addTextField("E-mail*", "email");
		addTextField("Mobile*", "mobile");
		addTextField("Fixe", "fixe");
		addTextField("Adresse", "adresse");
		addTextField("Ville*", "ville");
		addTextField("Mot de passe*", "password");
		
		addLabel("* : champs obligatoires", ContentMode.HTML);

	}




	protected void performSauvegarder()
	{
//		String email = (String) item.getItemProperty("email").getValue();
//		String msg = new PasswordManager().sendMailForResetPassword(email);
//		if (msg==null)
//		{
//			Notification.show("Votre compte à bien été créé. Vous pouvez maintenant vous connecter.", Type.WARNING_MESSAGE);
//		}
//		else
//		{
//			NotificationHelper.displayNotification(msg);
//		}
	}

}
