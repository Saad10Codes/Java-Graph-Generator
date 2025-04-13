package pl.poznan.put.dentalsurgery.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.AutoPopulatingList;

import pl.poznan.put.dentalsurgery.components.DateSerializer;

/**
 * 
 * @author Kuciu
 * 
 */

public class Patient {
	private Long patientId;

	@NotBlank
	private String name;

	@NotBlank
	private String surname;

	@NotNull
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Past
	private Date bornDate;

	@NotBlank
	private String city;

	@NotBlank
	private String street;

	@NotBlank
	@Length(min = 11, max = 11)
	private String pesel;

	private char gender;

	private List<PhoneNumber> phoneNumbers = new AutoPopulatingList<PhoneNumber>(
			PhoneNumber.class);
	private List<Illness> illnesses = new ArrayList<Illness>();
	private List<Medication> medications = new ArrayList<Medication>();
	private List<Visit> visits = new ArrayList<Visit>();

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(final Long patientId) {
		this.patientId = patientId;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getBornDate() {
		return bornDate;
	}

	public void setBornDate(final Date bornDate) {
		this.bornDate = bornDate;
	}

	public List<Visit> getVisits() {
		return visits;
	}

	public void setVisits(final List<Visit> visits) {
		this.visits = visits;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(final String street) {
		this.street = street;
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(final List<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public List<Illness> getIllnesses() {
		return illnesses;
	}

	public void setIllnesses(final List<Illness> illnesses) {
		this.illnesses = illnesses;
	}

	public List<Medication> getMedications() {
		return medications;
	}

	public void setMedications(final List<Medication> medications) {
		this.medications = medications;
	}

	public char getGender() {
		return gender;
	}

	public void setGender(final char gender) {
		this.gender = gender;
	}

	public String getPesel() {
		return pesel;
	}

	public void setPesel(final String pesel) {
		this.pesel = pesel;
	}
}
