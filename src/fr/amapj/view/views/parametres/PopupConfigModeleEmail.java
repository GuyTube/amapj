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
 package fr.amapj.view.views.parametres;

import java.util.Collection;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.common.Dictionary;
import fr.amapj.common.DictionaryEnum;
import fr.amapj.model.models.param.ModeleEmailEnum;
import fr.amapj.model.models.param.SmtpType;
import fr.amapj.service.services.mailer.MailerService;
import fr.amapj.service.services.mailer.ModeleEmailDTO;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.producteur.ProducteurDTO;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.enumselector.EnumSearcher;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.popup.formpopup.validator.NotNullValidator;
import fr.amapj.view.engine.popup.okcancelpopup.OKCancelMessagePopup;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Permet à un utilisateur de mettre à jour les modèles de mail
 * 
 *
 */
public class PopupConfigModeleEmail extends WizardFormPopup
{

	private ModeleEmailDTO dto;
	
	private Searcher modeleEmailBox;
	
	private TextField sujet;
	private RichTextArea contenu;
	private TextArea contenuHTML;
	private ComboBox param;
	private TextField paramtf;
	protected Button switchHTML;
	private boolean htmlMode=false;
	
	// L'utilisateur est il adminFull ? 
	private boolean adminFull;
	
	public enum Step
	{
		MODELE_MAIL ;
	}

	/**
	 * 
	 */
	public PopupConfigModeleEmail()
	{
		setWidth(80);
		popupTitle = "Modification des modèles de mail";
		this.dto = new ModeleEmailDTO();

		item = new BeanItem<ModeleEmailDTO>(this.dto) ;
		
		adminFull = SessionManager.getSessionParameters().isAdminFull();
		
	}
	
	@Override
	protected void configure()
	{
		add(Step.MODELE_MAIL,()->addFieldModeleEmail());
	}
	
	private void addFieldModeleEmail()
	{
		// Titre
		setStepTitle("Mise à jour des modèles de mails");
				
		modeleEmailBox = new Searcher(SearcherList.MODELEEMAIL);
		modeleEmailBox.setImmediate(true);
		modeleEmailBox.addValueChangeListener(e->handleModeleEmailChange());
		form.addComponent(modeleEmailBox);
		
		param = new ComboBox("Paramètres substituables");
		param.addValueChangeListener(e->handleParametreChange());
		param.setWidth("300px");
		param.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
		form.addComponent(param);
		
		paramtf = new TextField();
		paramtf.setCaption("Code à copier");
		paramtf.setWidth("300px");
		paramtf.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
		form.addComponent(paramtf);
		
		sujet = addTextField("Titre du mail", "titreEmail");
		
		if( htmlMode ) {
			contenu =  addRichTextAeraField("Contenu du mail", "contenuEmail");
			binder.unbind(contenu);
			contenuHTML =  addTextAeraField("Contenu du mail (HTML)", "contenuEmail");
			contenu.setVisible(false);

		} else {
			contenuHTML =  addTextAeraField("Contenu du mail (HTML)", "contenuEmail");
			binder.unbind(contenuHTML);
			contenu =  addRichTextAeraField("Contenu du mail", "contenuEmail");
			contenuHTML.setVisible(false);
		}
		contenuHTML.setHeight(8, Unit.CM);
		contenu.setHeight(8, Unit.CM);
		String bText = htmlMode ? "Editer le texte" : "Editer l'HTML";
		switchHTML = new Button(bText, e->handleHTMLSwitch());
		form.addComponent(switchHTML);
	}
	

	private void handleHTMLSwitch() {
		String contenuEdit;

		if( htmlMode) {
			contenuEdit = contenuHTML.getValue();
			contenuHTML.setVisible(false);
			binder.unbind(contenuHTML);
			binder.bind(contenu, "contenuEmail");
			contenu.setValue(contenuEdit);
			contenu.setVisible(true);
			switchHTML.setCaption("Editer l'HTML");
		} else {
			contenuEdit = contenu.getValue();
			contenu.setVisible(false);
			binder.unbind(contenu); 
			binder.bind(contenuHTML, "contenuEmail");
			contenuHTML.setValue(contenuEdit);
			contenuHTML.setVisible(true);
			switchHTML.setCaption("Editer le texte");
		}
		htmlMode = !htmlMode;
		
	}
	
	private void handleParametreChange() {
		System.out.println(param.getValue());
		paramtf.setValue("#"+param.getValue()+"#");
	}
	
	@Override
	protected void performSauvegarder()
	{
		Long modeleId = (Long) modeleEmailBox.getConvertedValue();

		if( modeleId != null && this.dto.getContenuEmail() != contenu.getValue() || this.dto.getTitreEmail() != sujet.getValue())  {
			this.dto.setContenuEmail(contenu.getValue());
			this.dto.setTitreEmail(sujet.getValue());
		}
		
		MailerService.updateModeleEmail(this.dto);
	}
	
	private void handleModeleEmailChange()
	{
		if( this.dto.getContenuEmail() != contenu.getValue() || this.dto.getTitreEmail() != sujet.getValue()) {
			String title = "Annuler ?";
			String htmlMessage = "Voulez-vous abandonner vos modifications ?<br/>";
			
			OKCancelMessagePopup popup = new OKCancelMessagePopup(title, htmlMessage, ()->modeleEmailBox.setEnabled(true));
			OKCancelMessagePopup.open(popup);		
		}
		Long modeleId = (Long) modeleEmailBox.getConvertedValue();
		this.dto = MailerService.getModeleEmailDTO(modeleId);
		if (this.dto!=null)
		{
			item = new BeanItem<ModeleEmailDTO>(this.dto);
			contenu.setValue(this.dto.contenuEmail);
			sujet.setValue(this.dto.titreEmail);
			EnumSearcher.resetEnum(param, dto.getDesignation().getWordList());
		}
	}
	

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
	
}
