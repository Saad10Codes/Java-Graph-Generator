package pl.poznan.put.dentalsurgery.model;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.poznan.put.dentalsurgery.service.PhoneNumberService;

public class PhoneNumberEditor extends PropertyEditorSupport {

	private PhoneNumberService phoneNumberService;

	public void setPhoneNumberService(
			final PhoneNumberService phoneNumberService) {
		this.phoneNumberService = phoneNumberService;
	}

	private static final Log LOGGER = LogFactory
			.getLog(PhoneNumberEditor.class);

	@Override
	public void setAsText(final String text) {
		PhoneNumber phoneNumber = null;
		try {

			final long id = Long.parseLong(text);
			phoneNumber = phoneNumberService.getPhoneNumberById(id);
			if (phoneNumber == null) {
				if (id < 0) {
					phoneNumber = phoneNumberService.getPhoneNumberById(-id);
					if (phoneNumber != null) {
						phoneNumber.setPhoneId(id);
					}
				}
			}
		} catch (final NumberFormatException e) {
			LOGGER.warn(e);
			final String number = text.replaceFirst("new", "");
			phoneNumber = new PhoneNumber();
			phoneNumber.setNumber(number);

		}
		setValue(phoneNumber);
	}

	@Override
	public String getAsText() {
		return null;

	}
}
