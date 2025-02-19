/*
 *  Copyright 2013-2050 Emmanuel BRUN (contact@amapj.fr)
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
 package fr.amapj.view.views.editionspe.bilanlivraison;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;

import fr.amapj.common.ObjectUtils;
import fr.amapj.common.ResourceUtils;
import fr.amapj.model.models.editionspe.EditionSpecifique;
import fr.amapj.model.models.editionspe.PageFormat;
import fr.amapj.model.models.editionspe.bilanlivraison.BilanLivraisonJson;
import fr.amapj.service.engine.generator.pdf.PdfHtmlUtils;
import fr.amapj.service.services.editionspe.EditionSpeDTO;
import fr.amapj.service.services.editionspe.EditionSpeService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.popup.formpopup.validator.IValidator;
import fr.amapj.view.engine.popup.formpopup.validator.NotNullValidator;
import fr.amapj.view.engine.popup.formpopup.validator.UniqueInDatabaseValidator;

/**
 * Permet le paramétrage des bilans de livraisons
 * 
 *
 */
public class BilanLivraisonEditorPart extends WizardFormPopup
{

	private BilanLivraisonJson  etiquetteDTO;

	private boolean create;
	
	private BilanLivraisonTemplate templateSelected = null;


	public enum Step
	{
		GENERAL, MARGES , TEXT;
	}

	/**
	 * 
	 */
	public BilanLivraisonEditorPart(boolean create,EditionSpeDTO p)
	{
		this.create = create;
		
		setWidth(90);
		setHeight("90%");
		
		if (create)
		{
			popupTitle = "Création d'un modèle de bilan de livraison";
			this.etiquetteDTO = new BilanLivraisonJson();
		}
		else
		{
			popupTitle = "Modification d'un modèle de bilan de livraison";
			this.etiquetteDTO = (BilanLivraisonJson) new EditionSpeService().load(p.id);
		}	
		
		item = new BeanItem<BilanLivraisonJson>(this.etiquetteDTO);

	}
	
	@Override
	protected void configure()
	{
		add(Step.GENERAL,()->addFieldGeneral());
		add(Step.MARGES,()->addFieldMarges());
		add(Step.TEXT,()->addFieldText());
	}

	private void addFieldGeneral()
	{
		// Titre
		setStepTitle("les informations générales");
		
		// Champ 1
		IValidator uniq = new UniqueInDatabaseValidator(EditionSpecifique.class,"nom",etiquetteDTO.getId());
		addTextField("Nom", "nom",uniq);
		
		//
		if (create)
		{
			addComboEnumField("Modele de départ", "template",new NotNullValidator());			
			
			String content = "Plusieurs modéles de départ sont proposés<br>Il est conseillé d'essayer les différents modèles, de voir celui qui est le plus proche de votre besoin, puis de l'adapter ensuite<br/>";
			addLabel(content, ContentMode.HTML);
		}

		
	}
	
	private void addFieldMarges()
	{
		// On verifie si l'opérateur a changé le template (pour gerer le cas ou l'operateur fait des allers retours entre etape 1 et 2 et 3)		
		if ( create && ObjectUtils.equals(templateSelected,etiquetteDTO.getTemplate())==false) 
		{
			String filename = "template/"+etiquetteDTO.getTemplate().name().toLowerCase()+".html";
			String template = ResourceUtils.toString(this, filename);
			etiquetteDTO.setText(template);
			
			etiquetteDTO.setMargeBas(10);
			etiquetteDTO.setMargeHaut(10);
			etiquetteDTO.setMargeDroite(10);
			etiquetteDTO.setMargeGauche(10);
			etiquetteDTO.setPageFormat(etiquetteDTO.getTemplate().getPageFormat());
			
			templateSelected = etiquetteDTO.getTemplate();
		}
		
		
		// Titre
		setStepTitle("les informations de marges et de disposition");
		
		addIntegerField("Marge droite (en mm)", "margeDroite");
		addIntegerField("Marge gauche (en mm)", "margeGauche");
		addIntegerField("Marge en haut (en mm)", "margeHaut");
		addIntegerField("Marge en bas (en mm)", "margeBas");
		
		PageFormat[] toExclude = { PageFormat.A3_PAYSAGE , PageFormat.A3_PORTRAIT};
		addComboEnumField("Disposition de la page","pageFormat",toExclude,new NotNullValidator());	
	}

	
	
	
	
	private void addFieldText()
	{
		// De façon systematique, on met à jour les champs de l'entete 
		String c = PdfHtmlUtils.updateHeaderAndBodyLineForCKEditor(etiquetteDTO.getText(),etiquetteDTO);
		etiquetteDTO.setText(c);
			
		// Titre
		setStepTitle("le contenu du bilan de livraison");
		
		addCKEditorFieldForDocument("text");    
	}
		
	

	@Override
	protected void performSauvegarder()
	{
		new EditionSpeService().update(etiquetteDTO, create);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
