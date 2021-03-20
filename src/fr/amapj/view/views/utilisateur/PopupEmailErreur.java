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
 package fr.amapj.view.views.utilisateur;


import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.services.mailer.HistoriqueEmailDTO;
import fr.amapj.view.engine.popup.corepopup.CorePopup;

/**
 * Popup 
 * 
 *
 */
public class PopupEmailErreur extends CorePopup
{

	TextField titre;
	RichTextArea zoneTexte;
	HistoriqueEmailDTO email;
	Button fermer;

	/**
	 * 
	 */

	public PopupEmailErreur(HistoriqueEmailDTO hm) 
	{
		init(hm);
	}
	
	private void init(HistoriqueEmailDTO hm) {
		setWidth(60);
		popupTitle = "Emails en erreur";
		email = hm;
	}
	
	/**
	 * 
	 */
	@Override
	protected void createContent(VerticalLayout contentLayout) {
		
		VerticalLayout layout = contentLayout;

		zoneTexte = new RichTextArea("Liste des emails en erreur pour cet envoi");
		zoneTexte.setWidth("100%");
		if( email != null ) {
			zoneTexte.setValue(email.getErrorMails()!=null ? email.getErrorMails():"");
		}
		zoneTexte.setReadOnly(true);
		layout.addComponent(zoneTexte);
		
		fermer = addButton(layout, "Fermer",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				close();
			}
		});
	}

	private TextField addTextField(VerticalLayout layout, String lib)
	{
		HorizontalLayout h = new HorizontalLayout();
		h.setSpacing(true);
		//h.setWidth("100%");
		
		TextField tf = new TextField();
		tf.setWidth("500px");
		
		Label l = new Label(lib);
		l.setWidth("150px");
		h.addComponent(l);
		h.addComponent(tf);
		layout.addComponent(h);
		return tf;
		
	}
	
	private Button addButton(Layout layout, String str,ClickListener listener)
	{
		Button b = new Button(str);
		b.addStyleName("primary");
		b.addClickListener(listener);
		layout.addComponent(b);
		return b;
	}
	
	
	private Label addLabel(VerticalLayout layout, String str)
	{
		Label tf = new Label(str);
		layout.addComponent(tf);
		return tf;

	}
	
	private Label addEmptyLine(VerticalLayout layout)
	{
		Label tf = new Label("<br/>",ContentMode.HTML);
		layout.addComponent(tf);
		return tf;

	}

	
	@Override
	protected void createButtonBar() {
		// TODO Auto-generated method stub
		
	}
	
}
