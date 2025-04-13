package pl.poznan.put.dentalsurgery.model;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UploadItem {
	@NotEmpty
	@Length(min = 1)
	private String description;
	@NotNull
	private CommonsMultipartFile fileData;

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public CommonsMultipartFile getFileData() {
		return fileData;
	}

	public void setFileData(final CommonsMultipartFile fileData) {
		this.fileData = fileData;
	}
}
