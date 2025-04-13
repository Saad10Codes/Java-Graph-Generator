package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import pl.poznan.put.dentalsurgery.model.ToothActivity;

public interface ToothActivityDictDao {

	long addToothActivity(ToothActivity toothActivity);

	void deleteToothActivity(ToothActivity toothActivity);

	void updateToothActivity(ToothActivity toothActivity);

	ToothActivity getToothActivityById(long toothActivityId);

	List<ToothActivity> getAllToothActivities();

}
