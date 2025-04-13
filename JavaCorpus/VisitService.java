package pl.poznan.put.dentalsurgery.service;

import pl.poznan.put.dentalsurgery.model.Patient;
import pl.poznan.put.dentalsurgery.model.Visit;

public interface VisitService {

	Visit getVisitById(Long visitId);

	long saveDeserializedVisit(Visit visit);
	
	Visit getLastVisit(Patient patient);
	
	Visit prepareNewVisit(Patient patient);

	void removeVisit(Visit visit);
	
	void sortVisitsByDate(Patient patient);
	
}
