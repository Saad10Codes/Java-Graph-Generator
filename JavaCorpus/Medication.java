package pl.poznan.put.dentalsurgery.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Medication {
	private long medicationId;
	@JsonIgnore
	private Patient patient;
	private String name;

	public Medication() {
		this(null);
	}

	public Medication(final Patient patient) {
		this(patient, null);
	}

	public Medication(final Patient patient, final String name) {
		this.patient = patient;
		this.name = name;
	}

	public long getMedicationId() {
		return medicationId;
	}

	public void setMedicationId(final long medicationId) {
		this.medicationId = medicationId;
	}

	@JsonIgnore
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(final Patient patient) {
		this.patient = patient;
	}

	public String getName() {
		return name;
	}

	public void setName(final String value) {
		this.name = value;
	}
}
