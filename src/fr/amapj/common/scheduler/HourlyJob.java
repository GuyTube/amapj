package fr.amapj.common.scheduler;

public class HourlyJob implements Runnable {
	public void run() {
	    // Do your Hourly job here.
	    System.out.println("Hourly Job trigged by scheduler");
	    sendReminderUncompletePermanence();
	    sendReminder24h12hBefore();
	    
	  }
	
	private void sendReminder24h12hBefore() {
	    // Y a t'il une livraison prevue dans les 24h
	    // Si oui
	    // 	Recuperer les utilisateurs abonnes a un rappel (non envoye) 24h avant
	    // 	Recuperer les utilisateurs abonnes a un rappel (non envoye) 12h avant
	    //	Pour chaque utilisateur constituer la liste les contrats concernes par une livraison
	    //	Generer le texte en se basant sur un modele
	    // 	Envoyer le mail
	
	}
	
	private void sendReminderUncompletePermanence() {
		// Recuperer le reminder permanence de la base
		// Y a t'il une permanence dans les X jours (configure dans le Reminder)
		// Si le reminder pour cette livraison n'a pas ete envoye
		// Envoyer un mail a toutes les personnes qui ne sont pas enregistrees
	
	}
}