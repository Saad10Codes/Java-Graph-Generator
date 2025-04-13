package pl.poznan.put.dentalsurgery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.poznan.put.dentalsurgery.model.Illness;
import pl.poznan.put.dentalsurgery.repository.IllnessDao;

@Service
public class IllnessServiceImpl implements IllnessService {

	private IllnessDao illnessDao;

	@Autowired
	public void setIllnessDao(final IllnessDao illnessDao) {
		this.illnessDao = illnessDao;
	}

	@Override
	@Transactional
	public Illness getIllnessById(final long illnessId) {
		return illnessDao.getIllnessById(illnessId);
	}

	@Override
	@Transactional
	public void delete(final Illness illness) {
		illnessDao.delete(illness);
	}

}
