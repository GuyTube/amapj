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
 package fr.amapj.view.views.saisiecontrat;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.common.CollectionUtils;
import fr.amapj.common.DateUtils;
import fr.amapj.common.FormatUtils;
import fr.amapj.service.services.mescontrats.ContratDTO;
import fr.amapj.service.services.mescontrats.ContratLigDTO;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.corepopup.CorePopup.ColorStyle;
import fr.amapj.view.engine.popup.messagepopup.MessagePopup;
import fr.amapj.view.engine.popup.okcancelpopup.OKCancelPopup;
import fr.amapj.view.views.saisiecontrat.ContratAboManager.ContratAbo;

/**
 * Popup pour la gestion des jokers
 *  
 */
public class PopupSaisieJoker extends OKCancelPopup
{
	// 
	private ContratAbo abo;
	
	private List<ComboBox> combos;
	
	private List<CheckBox> cboxes;
	
	private List<ContratLigDTO> nonModifiableJokers;
	
	
	private ContratDTO contratDTO;
	
	private Label titre;
	
	// Indique si on peut modifier les jokers ou si on est en mode read only 
	private boolean readOnly;

	/**
	 * 
	 */
	public PopupSaisieJoker(ContratAbo abo,ContratDTO contratDTO,boolean readOnly)
	{
		this.abo = abo;
		this.contratDTO = contratDTO;
		this.readOnly = readOnly;
		CollectionUtils.sort(abo.dateJokers, e->e.date);
		
		combos = new ArrayList<>();
		cboxes = new ArrayList<>();
		nonModifiableJokers = new ArrayList<>();
		
		popupTitle = "Gestion des jokers de mon contrat "+contratDTO.nom;
		
		
		saveButtonTitle = "OK";
		if (readOnly)
		{
			hasCancelButton = false;
		}		
	}
	
	
	@Override
	protected void createContent(VerticalLayout contentLayout)
	{
		String msg = getTitre(abo.dateJokers.size());
		
		titre = new Label(msg,ContentMode.HTML);
		contentLayout.addComponent(titre);
		
		GridLayout gl = new GridLayout(4,contratDTO.jokerNbMax);
		gl.setMargin(true);
		gl.setSpacing(true);
		
		SimpleDateFormat df = FormatUtils.getFullDate();
		
		for (int i = 0; i < contratDTO.jokerNbMax; i++)
		{
			ContratLigDTO dateJoker = (i<abo.dateJokers.size()) ? abo.dateJokers.get(i) : null;
			boolean isModifiable = isModifiable(dateJoker);
			//
			Label l1 = new Label("Joker "+(i+1));
			l1.setWidth("80px");
			gl.addComponent(l1, 0, i);
			
			//
			if (isModifiable && readOnly==false)
			{
				ComboBox box = createComboBox(dateJoker,df);
				box.setWidth("220px");
				gl.addComponent(box, 1, i);
				combos.add(box);
				CheckBox checkboxNonConnu;
				checkboxNonConnu = new CheckBox();
				checkboxNonConnu.setCaption("Je ne connais pas la date");
				checkboxNonConnu.addValueChangeListener(event -> {
					if( checkboxNonConnu.getValue() == true ) {
						box.select(box.getNullSelectionItemId());
						box.setReadOnly(true);
					} else {
						box.setReadOnly(false);
					}
					updateTitre();
				});
				cboxes.add(checkboxNonConnu);
				gl.addComponent(checkboxNonConnu, 3,i);
			}
			else
			{
				String caption = dateJoker==null ? "Non utilisé" : df.format(dateJoker.date);
				gl.addComponent(new Label(caption), 1, i);
				if (dateJoker!=null)
				{
					nonModifiableJokers.add(dateJoker);
				}
			}


			String l3msg = isModifiable ? "" :" (Non modifiable)";
			Label l3 = new Label(l3msg);
			gl.addComponent(l3, 2, i);
				
		}
		contentLayout.addComponent(gl);

		
	}

	private String getTitre(int nbJokers)
	{
		return new ContratAboManager().computeJokerMessage(contratDTO, nbJokers,true);
	}


	private boolean isModifiable(ContratLigDTO dateJoker)
	{
		if (dateJoker==null)
		{
			return true;
		}
		
		return new ContratAboManager().isModifiable(dateJoker,contratDTO,DateUtils.getLocalDate());
	}


	private ComboBox createComboBox(ContratLigDTO dateJoker, SimpleDateFormat df)
	{
		ComboBox comboBox = new ComboBox();
		comboBox.setImmediate(true);
		
		LocalDate now = DateUtils.getLocalDate();
		
		for (ContratLigDTO lig : contratDTO.contratLigs)
		{
			// 
			if (contratDTO.isFullExcludedLine(lig.i)==false)
			{
				// On ajoute uniquement les dates qui sont modifiables 
				if (new ContratAboManager().isModifiable(lig,contratDTO,now))
				{
					String caption = df.format(lig.date);
					comboBox.addItem(lig);
					comboBox.setItemCaption(lig, caption);	
				}
			}
		}

		if (dateJoker!=null)
		{
			comboBox.select(dateJoker);
		}
		
		comboBox.addValueChangeListener(e->updateTitre());
		
		return comboBox;
	}


	private void updateTitre()
	{
		int nbJokers = (int) combos.stream().filter(e->e.getValue()!=null).count();
		nbJokers = nbJokers + (int) cboxes.stream().filter(e->e.getValue()==true).count();
		titre.setValue(getTitre(nbJokers));
	}


	@Override
	protected boolean performSauvegarder()
	{
		if (readOnly)
		{
			return true;
		}
		
		abo.dateJokers.clear();
		abo.dateJokers.addAll(nonModifiableJokers);
		
		int idx = 0;
		for (ComboBox comboBox : combos)
		{
			ContratLigDTO lig = (ContratLigDTO) comboBox.getValue();
			CheckBox cb = cboxes.get(idx);
			if (lig!=null)
			{
				abo.dateJokers.add(lig);
			} else if(cb.getValue() == true ) {
				abo.dateJokers.add(getLastIdWithShift(comboBox.getItemIds(), idx));
			}
			
			idx++;
		}

		for( CheckBox cb : cboxes) {
			
		}
		int nbJokers = abo.dateJokers.size();
		CollectionUtils.removeDuplicate(abo.dateJokers);
		if( nbJokers != abo.dateJokers.size()) {
			MessagePopup popup = new MessagePopup("Erreur de saisie",ColorStyle.RED,"Veuillez supprimer les doublons");
			CorePopup.open(popup);
			return false;
		}
		
		CollectionUtils.sort(abo.dateJokers,e->e.date);
		
		return true;
	}

	private ContratLigDTO getLastIdWithShift(Collection<?> col, int shift) {
		
		int i = 0;
		int nbItemCol = col.size();
		for( Object c : col) {
			if(i == nbItemCol - 1 - shift) {
				return (ContratLigDTO) c;
			}
			i++;
		}
		return null;
	}
	
}
