package pl.poznan.put.dentalsurgery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.poznan.put.dentalsurgery.model.Attachment;
import pl.poznan.put.dentalsurgery.repository.AttachmentDao;

@Service
public class AttachmentServiceImpl implements AttachmentService {

	private AttachmentDao attachmentDao;

	@Override
	@Transactional
	public Long save(final Attachment attachment) {
		return attachmentDao.addAttachment(attachment);
	}

	public AttachmentDao getAttachmentDao() {
		return attachmentDao;
	}

	@Autowired
	public void setAttachmentDao(final AttachmentDao attachmentDao) {
		this.attachmentDao = attachmentDao;
	}

	@Override
	@Transactional
	public Attachment getById(final long attachmentId) {
		return attachmentDao.getById(attachmentId);
	}

}
