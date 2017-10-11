package fr.amapj.model.models.fichierbase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity

public class UtilisateurPerso extends Utilisateur {
	
	@NotNull
	@Size(min = 1, max = 100)
	@Column(length = 100)
	private String nomCheque;
	
	public UtilisateurPerso() {
		super();
	}

	public String getNomCheque() {
		return nomCheque;
	}

	public void setNomCheque(String nomCheque) {
		this.nomCheque = nomCheque;
	}
	

}
