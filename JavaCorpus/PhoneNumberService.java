package pl.poznan.put.dentalsurgery.service;

import pl.poznan.put.dentalsurgery.model.PhoneNumber;

public interface PhoneNumberService {
	PhoneNumber getPhoneNumberById(long phoneId);

	void deletePhoneNumberById(long phoneId);
}
