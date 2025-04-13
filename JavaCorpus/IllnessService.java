package pl.poznan.put.dentalsurgery.service;

import pl.poznan.put.dentalsurgery.model.Illness;

public interface IllnessService {

	Illness getIllnessById(long illnessId);

	void delete(Illness illness);
}
