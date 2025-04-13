package pl.poznan.put.dentalsurgery.model;

public class VisitActivity {
	private Long activityId;
	private String description;
	private Double price;
	private Double vat;

	public VisitActivity() {
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(final Long activityId) {
		this.activityId = activityId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

	public Double getVat() {
		return vat;
	}

	public void setVat(final Double vat) {
		this.vat = vat;
	}
}
