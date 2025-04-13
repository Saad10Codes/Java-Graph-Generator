package pl.poznan.put.dentalsurgery.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.poznan.put.dentalsurgery.model.Patient;
import pl.poznan.put.dentalsurgery.model.Tooth;
import pl.poznan.put.dentalsurgery.model.ToothActivity;
import pl.poznan.put.dentalsurgery.model.Visit;
import pl.poznan.put.dentalsurgery.model.VisitActivity;
import pl.poznan.put.dentalsurgery.repository.ToothActivityDictDaoImpl;
import pl.poznan.put.dentalsurgery.repository.ToothStateDictDaoImpl;
import pl.poznan.put.dentalsurgery.repository.VisitActivityDictDaoImpl;
import pl.poznan.put.dentalsurgery.repository.VisitDao;

@Service
public class VisitServiceImpl implements VisitService {
	
	/**
	 * Zbiór numerów zębów w notacji FDI
	 */
	private static final Set<Integer> teethNumbers = new HashSet<Integer>(Arrays.asList(
		/////// zęby stałe
		// prawa górna strona				// lewa górna strona
		18, 17, 16, 15, 14, 13, 12, 11, 	21, 22, 23, 24, 25, 26, 27, 28, 
		// prawa dolna strona				// lewa dolna strona
		48, 47, 46, 45, 44, 43, 42, 41,		31, 32, 33, 34, 35, 36, 37, 38,
		
		/////// zęby mleczne
					//prawa górna strona	// lewa górna strona
					55, 54, 53, 52, 51, 	61, 62, 63, 64, 65, 
					// prawa dolna strona	// lewa dolna strona
					85, 84, 83, 82, 81,		71, 72, 73, 74, 75
	));
	
	private static final Log LOGGER = LogFactory
			.getLog(VisitServiceImpl.class);	
	
	private VisitDao visitDao;
	private VisitActivityDictDaoImpl visitActivityDictDao;
	private ToothStateDictDaoImpl toothStateDictDaoImpl;
	private ToothActivityDictDaoImpl toothActivityDictDaoImpl;
	
	@Autowired
	public void setVisitDao(VisitDao visitDao) {
		this.visitDao = visitDao;
	}

	@Autowired
	public void setVisitActivityDictDao(
			VisitActivityDictDaoImpl visitActivityDictDao) {
		this.visitActivityDictDao = visitActivityDictDao;
	}

	@Autowired
	public void setToothStateDictDaoImpl(ToothStateDictDaoImpl toothStateDictDaoImpl) {
		this.toothStateDictDaoImpl = toothStateDictDaoImpl;
	}

	@Autowired
	public void setToothActivityDictDaoImpl(ToothActivityDictDaoImpl toothActivityDictDaoImpl) {
		this.toothActivityDictDaoImpl = toothActivityDictDaoImpl;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Visit getVisitById(Long visitId) {
		return visitDao.getVisitById(visitId);
	}
	
	/**
	 * Dodaje nową wizytę do bazy danych. Przeładowuje odpowiednie obiekty istniejące w bazie
	 * (metoda ta służy do dodawania nowych wizyt, które są świeżo po deserializacji z jsona)
	 */
	@Override
	@Transactional
	public long saveDeserializedVisit(Visit visit) {

		// czynności dodane podczas tworzenia nowej wizyty trzeba ponownie załadować, ponieważ nie są
		// zarządzane przez hibernate'a. Metoda getActivityId() zagwarantuje, że pojedynczy obiekt nie
		// zostanie wielokrotnie załadowany z bazy.
		
		if (visit.getActivities() != null) {
			Set<VisitActivity> visitActivitySet = new HashSet<VisitActivity>();
			for (VisitActivity activity : visit.getActivities()) {
				visitActivitySet.add(visitActivityDictDao
						.getVisitActivityById(activity.getActivityId()));
			}
			visit.setActivities(visitActivitySet);
		}
		
		// obiekty zębów powiązane z wizytami też mają swoje stany i czynności, więc i je trzeba 
		// załadować do kontekstu hibernate'a
		
		if (visit.getTeeth() != null) {
			for (Tooth tooth : visit.getTeeth()) {
				
				// czynności związane z zębem
				if (tooth.getActivities() != null) {
					Set<ToothActivity> toothActivities = new HashSet<ToothActivity>();
					for (ToothActivity toothActivity : tooth.getActivities()) {
						toothActivities.add(toothActivityDictDaoImpl
								.getToothActivityById(toothActivity
										.getActivityId()));
					}
					tooth.setActivities(toothActivities);
				}
				
				// stan dla całego zęba (wszystkie powierzchnie)
				if (tooth.getAllToothState() != null) {
					tooth.setAllToothState(toothStateDictDaoImpl
							.getToothStateById(tooth.getAllToothState()
									.getToothStateId()));
				}
				
				// powierzchnia 1
				if (tooth.getArea1State() != null) {
					tooth.setArea1State(toothStateDictDaoImpl
							.getToothStateById(tooth.getArea1State()
									.getToothStateId()));
				}
				
				// powierzchnia 2
				if (tooth.getArea2State() != null) {
					tooth.setArea2State(toothStateDictDaoImpl
							.getToothStateById(tooth.getArea2State()
									.getToothStateId()));
				}
				
				// powierzchnia 3
				if (tooth.getArea3State() != null) {
					tooth.setArea3State(toothStateDictDaoImpl
							.getToothStateById(tooth.getArea3State()
									.getToothStateId()));
				}

				// powierzchnia 4
				if (tooth.getArea4State() != null) {
					tooth.setArea4State(toothStateDictDaoImpl
							.getToothStateById(tooth.getArea4State()
									.getToothStateId()));
				}
				
				// powierzchnia 5
				if (tooth.getArea5State() != null) {
					tooth.setArea5State(toothStateDictDaoImpl
							.getToothStateById(tooth.getArea5State()
									.getToothStateId()));
				}
				
				// powierzchnia 6
				if (tooth.getArea6State() != null) {
					tooth.setArea6State(toothStateDictDaoImpl
							.getToothStateById(tooth.getArea6State()
									.getToothStateId()));
				}
				
				// powierzchnia 7
				if (tooth.getArea7State() != null) {
					tooth.setArea7State(toothStateDictDaoImpl
							.getToothStateById(tooth.getArea7State()
									.getToothStateId()));
				}
				
				// powierzchnia 8
				if (tooth.getArea8State() != null) {
					tooth.setArea8State(toothStateDictDaoImpl
							.getToothStateById(tooth.getArea8State()
									.getToothStateId()));
				}
			}
		}
		return visitDao.addVisit(visit);
	}

	@Override
	@Transactional(readOnly = true)
	public Visit getLastVisit(Patient patient) {
		return visitDao.getLastVisitFromDb(patient);
	}
	
	/**
	 * Przygotowuje obiekt nowej wizyty
	 */
	@Override
	@Transactional(readOnly = true)
	public Visit prepareNewVisit(Patient patient) {
		
		Visit newVisit = new Visit(patient);
		
		// odczytujemy obecny stan zębów
		Visit lastVisit = visitDao.getLastVisitFromDb(patient);	
		Set<Integer> exisitingTeeth = new HashSet<Integer>(teethNumbers.size());
		if (lastVisit != null) {
			for (Tooth tooth : lastVisit.getTeeth()) {
				tooth.setActivities(new HashSet<ToothActivity>());
				tooth.setToothId(null);
				exisitingTeeth.add(tooth.getNumber());
			}
			newVisit.setTeeth(lastVisit.getTeeth());
		}
		
		Set<Integer> remainedTeeth = new HashSet<Integer>(teethNumbers);
		remainedTeeth.removeAll(exisitingTeeth);
		
		// sprawdzamy, czy obiekty wszystkich zębów w szczęce istnieją. Jeśli nie, 
		// tworzymy nowe
		for (Integer toothNumber : remainedTeeth) {
			Tooth newTooth = new Tooth();
			newTooth.setNumber(toothNumber);
			newVisit.getTeeth().add(newTooth);
		}
		
		return newVisit;
	}
	
	
	/**
	 * Usuwa wizytę
	 */
	@Override @Transactional
	public void removeVisit(Visit visit) {
		visitDao.removeVisit(visit);
	}

	/**
	 * Porządkuje listę wizyt danego pacjenta wg daty
	 */
	@Override
	public void sortVisitsByDate(Patient patient) {
	
		List<Visit> visits = patient.getVisits();
		if (visits == null) return;
		Collections.sort(visits, new Comparator<Visit>() {
			@Override
			public int compare(Visit o1, Visit o2) {
				if (o1 == null || o2 == null) 
					return 0;
				if (o1.getVisitDate().before(o2.getVisitDate()) ) 
					return 1;
				if (o1.getVisitDate().after(o2.getVisitDate()) )
					return -1;
				return 0;
			}
		});
		
	}
}
