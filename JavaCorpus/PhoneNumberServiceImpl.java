package pl.poznan.put.dentalsurgery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.poznan.put.dentalsurgery.model.PhoneNumber;
import pl.poznan.put.dentalsurgery.repository.PhoneNumberDao;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {
	@Autowired
	PhoneNumberDao phoneNumberDao;

	@Override
	@Transactional
	public PhoneNumber getPhoneNumberById(final long phoneId) {
		return phoneNumberDao.getPhoneNumberById(phoneId);
	}

	@Override
	@Transactional
	public void deletePhoneNumberById(final long phoneId) {
		final PhoneNumber phoneNumber = phoneNumberDao
				.getPhoneNumberById(phoneId);
		if (phoneNumber != null) {
			phoneNumberDao.delete(phoneNumber);
		}
	}

}
