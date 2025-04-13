package pl.poznan.put.dentalsurgery.service;

import pl.poznan.put.dentalsurgery.model.Medication;

public interface MedicationService {

	Medication getMedicationById(long medicationId);

	void delete(Medication medication);
}
