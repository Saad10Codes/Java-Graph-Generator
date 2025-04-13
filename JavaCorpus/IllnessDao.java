package pl.poznan.put.dentalsurgery.repository;

import pl.poznan.put.dentalsurgery.model.Illness;

public interface IllnessDao {

	Illness getIllnessById(long illnessId);

	void delete(Illness illness);

}
