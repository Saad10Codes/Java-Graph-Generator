package pl.poznan.put.dentalsurgery.repository;

import pl.poznan.put.dentalsurgery.model.Medication;

public interface MedicationDao {

	Medication getMedicationById(long medicationId);

	void delete(Medication medication);

}
