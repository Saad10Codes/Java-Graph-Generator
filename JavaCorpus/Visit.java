package pl.poznan.put.dentalsurgery.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonBackReference;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import pl.poznan.put.dentalsurgery.components.DateDeserializer;
import pl.poznan.put.dentalsurgery.components.DateSerializer;

public class Visit {
	private Long visitId;
	private Patient patient;
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date visitDate = new Date();
	private Set<VisitActivity> activities = new HashSet<VisitActivity>();
	private String comments;
	private Set<Tooth> teeth = new HashSet<Tooth>();

	private Set<Attachment> attachments;

	@JsonIgnore
	public Set<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(final Set<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Visit() {
	}

	public Set<Tooth> getTeeth() {
		return teeth;
	}

	public void setTeeth(final Set<Tooth> teeth) {
		this.teeth = teeth;
		for (final Tooth tooth : teeth) {
			tooth.setVisit(this);
		}
	}

	public String getComments() {
		return comments;
	}

	public void setComments(final String comments) {
		this.comments = comments;
	}

	@JsonBackReference
	public Patient getPatient() {
		return patient;
	}

	public void setPatient(final Patient patient) {
		this.patient = patient;
	}

	public Visit(final Patient patient) {
		this.patient = patient;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(final Long visitId) {
		this.visitId = visitId;
	}

	@JsonSerialize(using = DateSerializer.class)
	public Date getVisitDate() {
		return visitDate;
	}

	@JsonDeserialize(using = DateDeserializer.class)
	public void setVisitDate(final Date visitDate) {
		this.visitDate = visitDate;
	}

	public Set<VisitActivity> getActivities() {
		return activities;
	}

	public void setActivities(final Set<VisitActivity> activities) {
		this.activities = activities;
	}
}
