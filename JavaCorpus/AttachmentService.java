package pl.poznan.put.dentalsurgery.service;

import pl.poznan.put.dentalsurgery.model.Attachment;

public interface AttachmentService {

	Long save(Attachment attachment);

	Attachment getById(long attachmentId);
}
