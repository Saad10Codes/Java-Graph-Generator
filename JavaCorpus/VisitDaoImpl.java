package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import pl.poznan.put.dentalsurgery.model.Patient;
import pl.poznan.put.dentalsurgery.model.Visit;

@Repository
public class VisitDaoImpl extends AbstractDao<Visit> implements VisitDao {

	@Override
	public Visit getVisitById(final Long visitId) {
		return (Visit) sessionFactory.getCurrentSession().get(Visit.class,
				visitId);
	}
	
	@Override
	public long addVisit(final Visit visit) {
		return (Long) this.sessionFactory.getCurrentSession().save(visit);
	}

	@Override
	public void updateVisit(final Visit visitFromDb) {
		sessionFactory.getCurrentSession().update(visitFromDb);		
	}

	@Override
	public void removeVisit(final Visit visitFromDb) {
		sessionFactory.getCurrentSession().delete(visitFromDb);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Visit> getAll() {
		final Criteria criteria = this.sessionFactory.getCurrentSession()
				.createCriteria(Visit.class);
		criteria.addOrder(Order.desc("visitDate"));
		return criteria.list();
	}
	
	@Override
	public List<Visit> getAllVisits() {
		return getAll();
	}
	
	/**
	 * Lista wizyt pacjenta w porządku malejącym
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Visit> getAllVisits(Patient patient) {
		final Criteria criteria = this.sessionFactory.getCurrentSession()
				.createCriteria(Visit.class);
		criteria.addOrder(Order.desc("visitDate")).add(
				Restrictions.eq("patient.patientId", patient.getPatientId()));
		List<Visit> visits = criteria.list();
		return visits;
	}

	@Override
	public Visit getLastVisitFromDb(Patient patient) {
		List<Visit> visits = this.getAllVisits(patient);
		if (visits != null && visits.size() > 0)
			return visits.get(0);
		return null;
	}

	
	
}
