package pl.poznan.put.dentalsurgery.model;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.poznan.put.dentalsurgery.service.MedicationService;

public class MedicationEditor extends PropertyEditorSupport {

	private static final Log LOGGER = LogFactory.getLog(IllnessEditor.class);

	private MedicationService medicationService;

	public void setMedicationService(final MedicationService medicationService) {
		this.medicationService = medicationService;
	}

	@Override
	public void setAsText(final String text) throws IllegalArgumentException {
		Medication medication = null;
		try {
			final long id = Long.parseLong(text);
			medication = medicationService.getMedicationById(id);
			if (medication == null) {
				if (id < 0) {
					medication = medicationService.getMedicationById(-id);
					if (medication != null) {
						medication.setMedicationId(id);
					}
				}
			}
		} catch (final NumberFormatException e) {
			LOGGER.warn(e);
			medication = new Medication();
			final String name = text.replaceFirst("new", "");
			medication.setName(name);
		}
		setValue(medication);

	}
}
