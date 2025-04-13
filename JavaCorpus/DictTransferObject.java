package pl.poznan.put.dentalsurgery.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import pl.poznan.put.dentalsurgery.model.ToothActivity;
import pl.poznan.put.dentalsurgery.model.ToothState;
import pl.poznan.put.dentalsurgery.model.VisitActivity;

/**
 * Obiekt transferowy służący do zasilenia formularza wizyt wszystkimi potrzebnymi słownikami.
 * @author unv
 *
 */
public class DictTransferObject {

	private Collection<ToothActivity> toothActivities;
	private Collection<VisitActivity> visitActivities;
	private Collection<ToothState> toothStates;

	public Collection<ToothActivity> getToothActivities() {
		return toothActivities;
	}

	public void setToothActivities(Collection<ToothActivity> toothActivities) {
		this.toothActivities = toothActivities;
	}

	public Collection<VisitActivity> getVisitActivities() {
		return visitActivities;
	}

	public void setVisitActivities(Collection<VisitActivity> visitActivities) {
		this.visitActivities = visitActivities;
	}

	public Collection<ToothState> getToothStates() {
		return toothStates;
	}

	public void setToothStates(Collection<ToothState> toothStates) {
		this.toothStates = toothStates;
	}

}
