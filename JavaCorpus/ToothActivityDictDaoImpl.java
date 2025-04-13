package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import pl.poznan.put.dentalsurgery.model.ToothActivity;

@Repository
public class ToothActivityDictDaoImpl extends AbstractDao<ToothActivity>
		implements ToothActivityDictDao {

	@Override
	public long addToothActivity(final ToothActivity toothActivity) {
		return (Long) sessionFactory.getCurrentSession().save(toothActivity);
	}

	@Override
	public void deleteToothActivity(final ToothActivity toothActivity) {
		sessionFactory.getCurrentSession().delete(toothActivity);
	}

	@Override
	public void updateToothActivity(final ToothActivity toothActivity) {
		sessionFactory.getCurrentSession().update(toothActivity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ToothActivity> getAll() {
		return this.sessionFactory.getCurrentSession()
				.createQuery("from ToothActivity").list();
	}

	@Override
	public List<ToothActivity> getAllToothActivities() {
		return getAll();
	}

	@Override
	public ToothActivity getToothActivityById(final long toothActivityId) {
		return (ToothActivity) sessionFactory.getCurrentSession().get(
				ToothActivity.class, toothActivityId);
	}

}
