package pl.poznan.put.dentalsurgery.model;

import java.beans.PropertyEditorSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pl.poznan.put.dentalsurgery.service.IllnessService;

public class IllnessEditor extends PropertyEditorSupport {

	private static final Log LOGGER = LogFactory.getLog(IllnessEditor.class);

	private IllnessService illnessService;

	public void setIllnessService(final IllnessService illnessService) {
		this.illnessService = illnessService;
	}

	@Override
	public void setAsText(final String text) {
		Illness illness = null;
		try {
			final long id = Long.parseLong(text);
			illness = illnessService.getIllnessById(id);
			if (illness == null) {
				if (id < 0) {
					illness = illnessService.getIllnessById(-id);
					if (illness != null) {
						illness.setIllnessId(id);
					}
				}
			}
		} catch (final NumberFormatException e) {
			LOGGER.warn(e);
			illness = new Illness();
			final String name = text.replaceFirst("new", "");
			illness.setName(name);
		}
		setValue(illness);
	}
}
