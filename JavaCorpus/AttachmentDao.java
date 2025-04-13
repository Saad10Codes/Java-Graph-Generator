package pl.poznan.put.dentalsurgery.repository;

import pl.poznan.put.dentalsurgery.model.Attachment;

public interface AttachmentDao {

	Long addAttachment(Attachment attachment);

	Attachment getById(long attachmentId);

}
