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
 package fr.amapj.view.views.visiteamap;

import java.text.SimpleDateFormat;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.common.FormatUtils;
import fr.amapj.model.models.param.paramecran.PEMesLivraisons;
import fr.amapj.service.services.edgenerator.excel.emargement.EGFeuilleEmargement;
import fr.amapj.service.services.meslivraisons.JourLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.JourLivraisonsDTO.InfoPermanence;
import fr.amapj.service.services.meslivraisons.MesLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.MesLivraisonsService;
import fr.amapj.service.services.meslivraisons.ProducteurLivraisonsDTO;
import fr.amapj.service.services.meslivraisons.QteProdDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.excelgenerator.LinkCreator;
import fr.amapj.view.engine.menu.MenuList;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.template.FrontOfficeView;
import fr.amapj.view.engine.tools.BaseUiTools;
import fr.amapj.view.views.common.gapviewer.AbstractGapViewer;
import fr.amapj.view.views.common.gapviewer.GapViewerUtil;
import fr.amapj.view.views.common.gapviewer.WeekViewer;
import fr.amapj.view.views.mescontrats.TelechargerMesContrat;


/**
 * Viste de l'AMAP et des produits proposés
 *
 */
@SuppressWarnings("serial")
public class VisiteAmapView extends FrontOfficeView implements PopupListener
{
	
	static private String LABEL_DATEJOURLIV = "datejourliv";
	static private String LABEL_QTEPRODUIT = "qteproduit";	
	static private String PANEL_UNJOUR = "unjour";

	private VerticalLayout planning;
	
	private VerticalLayout livraison;
	
	private AbstractGapViewer semaineViewer;
	
	private SimpleDateFormat df1 = FormatUtils.getFullDate();

	
	@Override
	public String getMainStyleName()
	{
		return "livraison";
	}

	/**
	 * 
	 */
	@Override
	public void enter()
	{
		PEMesLivraisons peMesLivraisons = (PEMesLivraisons) new ParametresService().loadParamEcran(MenuList.MES_LIVRAISONS);
		
		semaineViewer = GapViewerUtil.createGapWiever(peMesLivraisons.modeAffichage, this);
		addComponent(semaineViewer.getComponent());
		
		VerticalLayout central = new VerticalLayout();
		addComponent(central);
		
		planning = new VerticalLayout();
		central.addComponent(planning);
		livraison = new VerticalLayout();
		central.addComponent(livraison);
		
		onPopupClose();
	}


	

	@Override
	public void onPopupClose()
	{
		MesLivraisonsDTO res = new MesLivraisonsService().getMesLivraisons(semaineViewer.getDateDebut(),semaineViewer.getDateFin(),SessionManager.getUserRoles(),SessionManager.getUserId());
		
		// Pour la semaine, ajout des planning mensuels de distribution
		/*planning.removeAllComponents();
		for (EGFeuilleEmargement planningMensuel : res.planningMensuel)
		{
			planning.addComponent(LinkCreator.createLink(planningMensuel));
		}
		if (res.planningMensuel.size()>0)
		{
			BaseUiTools.addEmptyLine(planning);
		}*/
		
		
		// Pour chaque jour, ajout des informations permanence et produits livrés
		livraison.removeAllComponents();
		for (JourLivraisonsDTO jour : res.jours)
		{
			VerticalLayout vl = BaseUiTools.addPanel(livraison, PANEL_UNJOUR);
			
			String dateMessage = df1.format(jour.date);
			BaseUiTools.addStdLabel(vl, dateMessage, LABEL_DATEJOURLIV);
			
			
			for (ProducteurLivraisonsDTO producteurLiv : jour.producteurs)
			{
				BaseUiTools.addBandeau(vl, producteurLiv.modeleContrat+"<ul><li>Produit 2</li><li>Produit 1</li></ul>", "nomcontrat");
				
				for (QteProdDTO cell : producteurLiv.produits)
				{
					String content = cell.qte+" "+cell.nomProduit+" , "+cell.conditionnementProduit;
					BaseUiTools.addStdLabel(vl, content, LABEL_QTEPRODUIT);
				}
				
				if (BaseUiTools.isCompactMode()==false)
				{
					vl.addComponent(new Label("<br/>",ContentMode.HTML));
				}
				
			}
		}	
	}
	
	


}
