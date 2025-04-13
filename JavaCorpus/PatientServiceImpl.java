package pl.poznan.put.dentalsurgery.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.poznan.put.dentalsurgery.model.Illness;
import pl.poznan.put.dentalsurgery.model.Medication;
import pl.poznan.put.dentalsurgery.model.Patient;
import pl.poznan.put.dentalsurgery.model.PhoneNumber;
import pl.poznan.put.dentalsurgery.repository.IllnessDao;
import pl.poznan.put.dentalsurgery.repository.MedicationDao;
import pl.poznan.put.dentalsurgery.repository.PatientDao;
import pl.poznan.put.dentalsurgery.repository.PhoneNumberDao;

@Service
public class PatientServiceImpl implements PatientService {

	private static final Log LOGGER = LogFactory
			.getLog(PatientServiceImpl.class);

	private PatientDao patientDao;

	private PhoneNumberDao phoneNumberDao;

	private MedicationDao medicationDao;

	private IllnessDao illnessDao;

	public PhoneNumberDao getPhoneNumberDao() {
		return phoneNumberDao;
	}

	@Autowired
	public void setPhoneNumberDao(final PhoneNumberDao phoneNumberDao) {
		this.phoneNumberDao = phoneNumberDao;
	}

	@Autowired
	public void setMedicationDao(final MedicationDao medicationDao) {
		this.medicationDao = medicationDao;
	}

	@Autowired
	public void setIllnessDao(final IllnessDao illnessDao) {
		this.illnessDao = illnessDao;
	}

	public PatientDao getPatientDao() {
		return patientDao;
	}

	@Autowired
	public void setPatientDao(final PatientDao patientDao) {
		this.patientDao = patientDao;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Patient> getAllPatients() {
		return patientDao.getAllPatients();
	}

	@Override
	@Transactional(readOnly = true)
	public Patient getPatientById(final long patientId) {
		return patientDao.getPatientById(patientId);
	}

	@Override
	@Transactional
	public long addPatient(final Patient patient) {
		return patientDao.addPatient(patient);
	}

	@Override
	@Transactional
	public void deletePatient(final Patient patient) {
		this.patientDao.deletePatient(patient);
	}

	@Override
	@Transactional
	public void updatePatient(final Patient patient) {
		final List<PhoneNumber> phoneToRemove = new ArrayList<PhoneNumber>();
		if (patient.getPhoneNumbers() != null) {
			for (final PhoneNumber number : patient.getPhoneNumbers()) {
				if (number.getPhoneId() < 0) {
					number.setPhoneId(-number.getPhoneId());
					phoneNumberDao.delete(number);
					phoneToRemove.add(number);
				}
			}

			patient.getPhoneNumbers().removeAll(phoneToRemove);
		}
		final List<Medication> medicationsToRemove = new ArrayList<Medication>();
		if (patient.getMedications() != null) {
			for (final Medication number : patient.getMedications()) {
				if (number.getMedicationId() < 0) {
					number.setMedicationId(-number.getMedicationId());
					medicationDao.delete(number);
					medicationsToRemove.add(number);
				}
			}

			patient.getMedications().removeAll(medicationsToRemove);
		}
		final List<Illness> illnessesToRemove = new ArrayList<Illness>();
		if (patient.getIllnesses() != null) {
			for (final Illness number : patient.getIllnesses()) {
				if (number.getIllnessId() < 0) {
					number.setIllnessId(-number.getIllnessId());
					illnessDao.delete(number);
					illnessesToRemove.add(number);
				}
			}

			patient.getIllnesses().removeAll(illnessesToRemove);
		}
		this.patientDao.updatePatient(patient);
	}
}
