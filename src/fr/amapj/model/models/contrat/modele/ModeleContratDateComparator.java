package fr.amapj.model.models.contrat.modele;

import java.util.Comparator;

import fr.amapj.common.DateUtils;

public class ModeleContratDateComparator implements  Comparator<ModeleContratDate> {

	@Override
	public int compare(ModeleContratDate o1, ModeleContratDate o2) {
		// getDeltaDelay seems to suppose that firstDate is after the second one
		int delta = DateUtils.getDeltaDay(o1.getDateLiv(), o2.getDateLiv());
		if( delta == 0) {
			return 0;
		} else if(delta > 0) {
			return 1;
		} else {
			return -1;
		}
	}


}
