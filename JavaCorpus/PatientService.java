package pl.poznan.put.dentalsurgery.service;

import java.util.Collection;

import pl.poznan.put.dentalsurgery.model.Patient;

public interface PatientService {
	Collection<Patient> getAllPatients();

	Patient getPatientById(long patientId);

	long addPatient(Patient patient);

	void deletePatient(Patient patient);

	void updatePatient(Patient patient);

}
