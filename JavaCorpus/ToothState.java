package pl.poznan.put.dentalsurgery.model;

public class ToothState {
	private long toothStateId;
	private String description;
	private boolean allTooth;

	public ToothState() {
	}

	public long getToothStateId() {
		return toothStateId;
	}

	public void setToothStateId(final long toothStateId) {
		this.toothStateId = toothStateId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public boolean isAllTooth() {
		return allTooth;
	}

	public void setAllTooth(final boolean allTooth) {
		this.allTooth = allTooth;
	}
}
