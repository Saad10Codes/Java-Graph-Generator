package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import pl.poznan.put.dentalsurgery.model.Medication;

@Repository
public class MedicationDaoImpl extends AbstractDao<Medication> implements
		MedicationDao {

	@Override
	public Medication getMedicationById(final long medicationId) {
		return (Medication) sessionFactory.getCurrentSession().get(
				Medication.class, medicationId);

	}

	@Override
	public void delete(final Medication medication) {
		this.sessionFactory.getCurrentSession().delete(medication);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Medication> getAll() {
		return this.sessionFactory.getCurrentSession()
				.createQuery("from Medication").list();
	}

}
