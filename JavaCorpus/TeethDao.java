package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import pl.poznan.put.dentalsurgery.model.Tooth;
import pl.poznan.put.dentalsurgery.model.Visit;

public interface TeethDao {
	List<Tooth> getAllTeeth();

	long addTooth(Tooth tooth);

	void deleteTooth(Tooth tooth);

	List<Tooth> getTeethByVisit(Visit visit);

	void upadateTooth(Tooth tooth);

	Tooth getToothById(long toothId);
}
