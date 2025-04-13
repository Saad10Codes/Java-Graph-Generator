package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import pl.poznan.put.dentalsurgery.model.Patient;
import pl.poznan.put.dentalsurgery.model.Visit;

public interface VisitDao {

	long addVisit(Visit visit);

	Visit getVisitById(Long visitId);

	void updateVisit(Visit visitFromDb);

	void removeVisit(Visit visitFromDb);
	
	List<Visit> getAllVisits();
	
	List<Visit> getAllVisits(Patient patient);
	
	Visit getLastVisitFromDb(Patient patient);
	
}
