package pl.poznan.put.dentalsurgery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.poznan.put.dentalsurgery.model.Medication;
import pl.poznan.put.dentalsurgery.repository.MedicationDao;

@Service
public class MedicationServiceImpl implements MedicationService {

	@Autowired
	public void setMedicationDao(final MedicationDao medicationDao) {
		this.medicationDao = medicationDao;
	}

	private MedicationDao medicationDao;

	@Override
	@Transactional
	public Medication getMedicationById(final long medicationId) {
		return medicationDao.getMedicationById(medicationId);
	}

	@Override
	@Transactional
	public void delete(final Medication medication) {
		medicationDao.delete(medication);
	}

}
