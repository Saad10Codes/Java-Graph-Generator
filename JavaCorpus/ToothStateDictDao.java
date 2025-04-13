package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import pl.poznan.put.dentalsurgery.model.ToothState;

public interface ToothStateDictDao {
	List<ToothState> getAllToothStates();

	long addToothState(ToothState toothState);

	void deleteToothState(ToothState toothState);

	ToothState getToothStateById(long toothStateId);

	void updateToothState(ToothState toothState);
}
