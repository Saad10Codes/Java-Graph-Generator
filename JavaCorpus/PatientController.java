package pl.poznan.put.dentalsurgery.web;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.poznan.put.dentalsurgery.model.Illness;
import pl.poznan.put.dentalsurgery.model.IllnessEditor;
import pl.poznan.put.dentalsurgery.model.Medication;
import pl.poznan.put.dentalsurgery.model.MedicationEditor;
import pl.poznan.put.dentalsurgery.model.Patient;
import pl.poznan.put.dentalsurgery.model.PhoneNumber;
import pl.poznan.put.dentalsurgery.model.PhoneNumberEditor;
import pl.poznan.put.dentalsurgery.service.IllnessService;
import pl.poznan.put.dentalsurgery.service.MedicationService;
import pl.poznan.put.dentalsurgery.service.PatientService;
import pl.poznan.put.dentalsurgery.service.PhoneNumberService;

@Controller
public class PatientController {
	private static final Log LOGER = LogFactory.getLog(PatientController.class);
	private PatientService patientService;
	private PhoneNumberService phoneNumberService;
	private IllnessService illnessService;

	private MedicationService medicationService;

	@Autowired
	public void setPatientService(final PatientService patientService) {
		this.patientService = patientService;
	}

	@Autowired
	public void setIllnessService(final IllnessService illnessService) {
		this.illnessService = illnessService;
	}

	@Autowired
	public void setMedicationService(final MedicationService medicationService) {
		this.medicationService = medicationService;
	}

	@Autowired
	public void setPhoneNumberService(
			final PhoneNumberService phoneNumberService) {
		this.phoneNumberService = phoneNumberService;
	}

	@InitBinder
	protected void initBinder(final HttpServletRequest request,
			final ServletRequestDataBinder binder) throws Exception {
		final PhoneNumberEditor phoneNumberEditor = new PhoneNumberEditor();
		phoneNumberEditor.setPhoneNumberService(phoneNumberService);
		final IllnessEditor illnessEditor = new IllnessEditor();
		illnessEditor.setIllnessService(illnessService);
		final MedicationEditor medicationEditor = new MedicationEditor();
		medicationEditor.setMedicationService(medicationService);

		binder.registerCustomEditor(PhoneNumber.class, phoneNumberEditor);
		binder.registerCustomEditor(Illness.class, illnessEditor);
		binder.registerCustomEditor(Medication.class, medicationEditor);
	}

	@RequestMapping(value = "/patients", method = RequestMethod.GET)
	public String listOfPatientsView(final Map<String, Object> model) {

		final Collection<Patient> patientList = patientService.getAllPatients();
		model.put("patientList", patientList);

		return "patients";
	}

	/* Dodawanie nowego pacjenta */

	@RequestMapping(value = "/patients/new", method = RequestMethod.GET)
	public String newPatientForm(final Model model) {
		model.addAttribute("patient", new Patient());
		model.addAttribute("editMode", false);
		return "patientForm";
	}

	@RequestMapping(value = "/patients/new", method = RequestMethod.POST)
	public String newPatientSubmit(
			@Valid @ModelAttribute final Patient patient,
			final BindingResult result) {

		if (result.hasErrors()) {
			return "patientForm";
		}

		linkListsWithPatient(patient);
		patientService.addPatient(patient);

		return "redirect:/patients";
	}

	/**
	 * Usunięcie pacjenta o podanym identyfikatorze
	 * 
	 * @param patientId
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/patients/{patientId}/delete", method = RequestMethod.POST)
	public @ResponseBody
	String deletePatient(@PathVariable final Long patientId,
			final HttpServletResponse response) {
		final Patient patient = patientService.getPatientById(patientId);
		if (patient != null) {
			patientService.deletePatient(patient);
			return "OK";
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "ERROR";
		}
	}

	/**
	 * Pobranie pacjenta do edycji o podanym identyfikatorze
	 * 
	 * @param patientId
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/patients/{patientId}/edit", method = RequestMethod.GET)
	public String editPatient(@PathVariable final Long patientId,
			final Model model, final HttpServletResponse response) {
		final Patient patient = patientService.getPatientById(patientId);
		if (patient != null) {
			/**
			 * Czyszczenie listy z nulli - przyleglość hibernate z
			 * wykorzystaniem list!
			 */
			clearLists(patient);

			model.addAttribute("patient", patient);
			return "patientForm";
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return "redirect:/patients";
		}
	}

	private void clearLists(final Patient patient) {
		patient.getPhoneNumbers().removeAll(Collections.singleton(null));
		patient.getIllnesses().removeAll(Collections.singleton(null));
		patient.getMedications().removeAll(Collections.singleton(null));
	}

	@RequestMapping(value = "/patients/{patientId}/edit", method = RequestMethod.POST)
	public String editPatientSubmit(
			@Valid @ModelAttribute final Patient patient,
			final BindingResult result, final HttpServletRequest request,
			final HttpServletResponse response) {

		if (result.hasErrors()) {
			return "patientForm";
		}

		linkListsWithPatient(patient);
		patientService.updatePatient(patient);

		return "redirect:/patients";
	}

	@RequestMapping(value = "/patients/{patientId}", method = RequestMethod.GET)
	public @ResponseBody
	Patient getPatient(@PathVariable final Long patientId, final Model model,
			final HttpServletResponse response) {
		final Patient patient = patientService.getPatientById(patientId);
		if (patient != null) {
			clearLists(patient);
			return patient;
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}
	}

	private void linkListsWithPatient(final Patient patient) {
		if (patient.getPhoneNumbers() != null) {
			for (final PhoneNumber phoneNumber : patient.getPhoneNumbers()) {
				// Property Editor może dodać do listy null :(
				if (phoneNumber != null) {
					phoneNumber.setPatient(patient);
				}
			}
		}
		if (patient.getMedications() != null) {
			for (final Medication medication : patient.getMedications()) {
				if (medication != null) {
					medication.setPatient(patient);
				}
			}
		}
		if (patient.getIllnesses() != null) {
			for (final Illness illness : patient.getIllnesses()) {
				if (illness != null) {
					illness.setPatient(patient);
				}
			}
		}
	}
	
	/**
	 * Drukuje informacje o pacjencie do pliku PDF
	 * @param patientId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/patients/{patientId}/patient-card.pdf", method = RequestMethod.GET)
	public String showPatientInfoPDF(@PathVariable Long patientId, Map<String, Object> model) {
		Patient patient = patientService.getPatientById(patientId);
		model.put("patient", patient);
		return "patientInfoPDFView";
	}
	
}
