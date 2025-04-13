package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import pl.poznan.put.dentalsurgery.model.PhoneNumber;

@Repository
public class PhoneNumberDaoImpl extends AbstractDao<PhoneNumber> implements
		PhoneNumberDao {

	@Override
	public PhoneNumber getPhoneNumberById(final long phoneId) {
		return (PhoneNumber) sessionFactory.getCurrentSession().get(
				PhoneNumber.class, phoneId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PhoneNumber> getAll() {
		return this.sessionFactory.getCurrentSession()
				.createQuery("from Patient").list();
	}

	@Override
	public void delete(final PhoneNumber phoneNumber) {
		this.sessionFactory.getCurrentSession().delete(phoneNumber);
	}

}
