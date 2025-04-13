package pl.poznan.put.dentalsurgery.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PhoneNumber {
	private long phoneId;
	@JsonIgnore
	private Patient patient;
	private String number;

	public PhoneNumber() {
		/**
		 * Wymagany przez hibernate
		 */
	}

	public PhoneNumber(final Patient patient) {
		this(patient, null);
	}

	public PhoneNumber(final Patient patient, final String number) {
		this.patient = patient;
		this.number = number;
	}

	public long getPhoneId() {
		return phoneId;
	}

	public void setPhoneId(final long phoneId) {
		this.phoneId = phoneId;
	}

	@JsonIgnore
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(final Patient patient) {
		this.patient = patient;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}
}
