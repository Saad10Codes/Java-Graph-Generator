package pl.poznan.put.dentalsurgery.model;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Illness {
	private long illnessId;
	@JsonIgnore
	private Patient patient;
	private String name;

	public Illness(final Patient patient, final String name) {
		this.patient = patient;
		this.name = name;
	}

	public Illness(final Patient patient) {
		this(patient, null);
	}

	public Illness() {
		this(null);
	}

	public long getIllnessId() {
		return illnessId;
	}

	public void setIllnessId(final long illnessId) {
		this.illnessId = illnessId;
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
