package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import pl.poznan.put.dentalsurgery.model.Illness;
import pl.poznan.put.dentalsurgery.model.Medication;

@Repository
public class IllnessDaoImpl extends AbstractDao<Medication> implements
		IllnessDao {

	@Override
	public Illness getIllnessById(final long illnessId) {
		return (Illness) sessionFactory.getCurrentSession().get(Illness.class,
				illnessId);
	}

	@Override
	public void delete(final Illness illness) {
		this.sessionFactory.getCurrentSession().delete(illness);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Medication> getAll() {
		return this.sessionFactory.getCurrentSession()
				.createQuery("from Illness").list();
	}

}
