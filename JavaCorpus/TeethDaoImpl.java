package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import pl.poznan.put.dentalsurgery.model.Tooth;
import pl.poznan.put.dentalsurgery.model.Visit;

@Repository
public class TeethDaoImpl extends AbstractDao<Tooth> implements TeethDao {
	@Override
	public List<Tooth> getAllTeeth() {
		return getAll();
	}

	@Override
	public long addTooth(final Tooth tooth) {
		return (Long) this.sessionFactory.getCurrentSession().save(tooth);
	}

	@Override
	public void deleteTooth(final Tooth tooth) {
		this.sessionFactory.getCurrentSession().delete(tooth);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tooth> getTeethByVisit(final Visit visit) {
		return this.sessionFactory.getCurrentSession()
				.createQuery("from Tooth where visit=?").setEntity(0, visit)
				.list();
	}

	@Override
	public Tooth getToothById(final long toothId) {
		return (Tooth) sessionFactory.getCurrentSession().get(Tooth.class,
				toothId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tooth> getAll() {
		return this.sessionFactory.getCurrentSession()
				.createQuery("from Tooth").list();
	}

	@Override
	public void upadateTooth(final Tooth tooth) {
		sessionFactory.getCurrentSession().update(tooth);
	}
}
