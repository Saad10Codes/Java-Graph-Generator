package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import pl.poznan.put.dentalsurgery.model.VisitActivity;

public interface VisitActivityDictDao {

	long addVisitActivity(VisitActivity visitActivity);

	void deleteVisitActivity(VisitActivity visitActivity);

	void updateVisitActivity(VisitActivity visitActivity);

	VisitActivity getVisitActivityById(long visitActivityId);

	List<VisitActivity> getAllVisitActivities();

}
