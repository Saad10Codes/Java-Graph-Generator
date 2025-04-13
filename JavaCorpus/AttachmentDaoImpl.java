package pl.poznan.put.dentalsurgery.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import pl.poznan.put.dentalsurgery.model.Attachment;

@Repository
public class AttachmentDaoImpl extends AbstractDao<Attachment> implements
		AttachmentDao {

	@Override
	public Long addAttachment(final Attachment attachment) {
		return (Long) this.sessionFactory.getCurrentSession().save(attachment);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Attachment> getAll() {
		return this.sessionFactory.getCurrentSession()
				.createQuery("from Attachment").list();
	}

	@Override
	public Attachment getById(final long attachmentId) {
		return (Attachment) sessionFactory.getCurrentSession().get(
				Attachment.class, attachmentId);
	}

}
