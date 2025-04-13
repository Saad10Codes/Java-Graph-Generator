package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import pl.poznan.put.dentalsurgery.model.ToothState;

@Repository
public class ToothStateDictDaoImpl extends AbstractDao<ToothState> implements
		ToothStateDictDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<ToothState> getAll() {
		return this.sessionFactory.getCurrentSession()
				.createQuery("from ToothState").list();
	}

	@Override
	public List<ToothState> getAllToothStates() {
		return getAll();
	}

	@Override
	public long addToothState(final ToothState toothState) {
		return (Long) this.sessionFactory.getCurrentSession().save(toothState);
	}

	@Override
	public void deleteToothState(final ToothState toothState) {
		this.sessionFactory.getCurrentSession().delete(toothState);
	}

	@Override
	public ToothState getToothStateById(final long toothStateId) {
		return (ToothState) sessionFactory.getCurrentSession().get(
				ToothState.class, toothStateId);
	}

	@Override
	public void updateToothState(final ToothState toothState) {
		sessionFactory.getCurrentSession().update(toothState);
	}

}
