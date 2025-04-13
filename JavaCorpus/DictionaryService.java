package pl.poznan.put.dentalsurgery.service;

import java.util.Collection;

import pl.poznan.put.dentalsurgery.model.ToothActivity;
import pl.poznan.put.dentalsurgery.model.ToothState;
import pl.poznan.put.dentalsurgery.model.VisitActivity;

public interface DictionaryService {

	DictTransferObject getAllDictionaries();

	Collection<VisitActivity> getVisitActivityDict();

	void addVisitActivity(VisitActivity activity);

	VisitActivity getVisitActivityById(Long activityId);

	void updateVisitActivity(VisitActivity activity);

	void deleteVisitActivity(VisitActivity visitActivityById);

	void addToothActivity(ToothActivity activity);

	void updateToothActivity(ToothActivity activity);

	ToothActivity getToothActivityById(Long activityId);

	void deleteToothActivity(ToothActivity toothActivity);

	Collection<ToothActivity> getToothActivityDict();

	Collection<ToothState> getToothStatesDict();

	void addToothState(ToothState state);

	void updateToothState(ToothState state);

	ToothState getToothStateById(Long stateId);

	void deleteToothState(ToothState toothState);

}
