package pl.poznan.put.dentalsurgery.repository;

import pl.poznan.put.dentalsurgery.model.PhoneNumber;

public interface PhoneNumberDao {
	PhoneNumber getPhoneNumberById(long phoneId);

	void delete(PhoneNumber phoneNumber);
}
