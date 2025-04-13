package pl.poznan.put.dentalsurgery.repository;

import java.util.Collection;

import pl.poznan.put.dentalsurgery.model.Patient;

public interface PatientDao {

	void deletePatient(Patient patient);

	long addPatient(Patient patient);

	Patient getPatientById(Long patientId);

	void updatePatient(Patient patientFromDb);

	Collection<Patient> getAllPatients();
}
