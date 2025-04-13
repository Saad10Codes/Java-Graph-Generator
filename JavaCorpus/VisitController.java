package pl.poznan.put.dentalsurgery.web;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.poznan.put.dentalsurgery.model.Attachment;
import pl.poznan.put.dentalsurgery.model.Patient;
import pl.poznan.put.dentalsurgery.model.ToothState;
import pl.poznan.put.dentalsurgery.model.UploadItem;
import pl.poznan.put.dentalsurgery.model.Visit;
import pl.poznan.put.dentalsurgery.service.AttachmentService;
import pl.poznan.put.dentalsurgery.service.DictionaryService;
import pl.poznan.put.dentalsurgery.service.PatientService;
import pl.poznan.put.dentalsurgery.service.VisitService;

@Controller
@RequestMapping("/patients/{patientId}/visits")
public class VisitController {

	private static final Log LOG = LogFactory.getLog(VisitController.class);
	private PatientService patientService;
	private VisitService visitService;
	private AttachmentService attachmentService;
	private DictionaryService dictionaryService;

	@Autowired
	public void setDictionaryService(final DictionaryService dictionaryService) {
		this.dictionaryService = dictionaryService;
	}

	public AttachmentService getAttachmentService() {
		return attachmentService;
	}

	@Autowired
	public void setAttachmentService(final AttachmentService attachmentService) {
		this.attachmentService = attachmentService;
	}

	@Autowired
	public void setPatientServiceService(final PatientService patientService) {
		this.patientService = patientService;
	}

	@Autowired
	public void setVisitService(final VisitService visitService) {
		this.visitService = visitService;
	}

	/**
	 * Dodaje do modelu pacjenta o ID podanym w URLu
	 * 
	 * @param patientId
	 * @return
	 */
	@ModelAttribute
	public Patient getPatient(@PathVariable final Long patientId) {
		return patientService.getPatientById(patientId);
	}

	/**
	 * Wyświetla stronę JSP z listą wizyt pacjenta, Na stronie tej można
	 * zobaczyć listę dotychczasowych wizyt pacjenta.
	 * 
	 * @return
	 */
	@RequestMapping({ "", "/" })
	public String visitsListView(@ModelAttribute final Patient patient,
			final Model model) {
		if (patient == null) {
			model.addAttribute("message", "Nie ma takiego pacjenta");
			return "error";
		}
		if (patient.getVisits() != null) {
			patient.getVisits().removeAll(Collections.singleton(null));
		}
		model.addAttribute("uploadItem", new UploadItem());
		return "visits";
	}

	/**
	 * Wyświetla stronę JSP na której można dodać nową wizytę
	 * 
	 * @param patientId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addVisitController(@ModelAttribute final Patient patient,
			final Model model) {
		if (patient == null) {
			model.addAttribute("message", "Nie ma takiego pacjenta");
			return "error";
		}
		return "add-visit";
	}

	/**
	 * Zapisuje do bazy obiekt wizyty, wysłany POSTem w postaci jsona
	 * 
	 * @param visit
	 * @param patient
	 * @return
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
	public @ResponseBody
	Visit saveVisit(@RequestBody final Visit visit,
			@ModelAttribute final Patient patient) {
		visit.setPatient(patient);
		final Long visitId = visitService.saveDeserializedVisit(visit);
		visitService.getVisitById(visitId);
		return visit;
	}

	/**
	 * Zwraca obiekt w postaci JSON konkretnej wizyty istniejącej w bazie danych
	 * 
	 * @param visitId
	 * @return
	 */
	@RequestMapping(value = "/{visitId}")
	public @ResponseBody
	Visit getVisit(@PathVariable final Long visitId,
			@ModelAttribute final Patient patient) {
		final Visit visit = visitService.getVisitById(visitId);
		// TODO sprawdzić, czy wizyta należy do pacjenta!
		return visit;
	}

	/**
	 * Zwraca JSONa z obiektem nowej wizyty. Obiekt ten tworzony jest w oparciu
	 * o ostatnia wizytę. Jeśli jest to pierwsza wizyta, tworzone są obiekty
	 * wszystkich zębów w notacji FDI.
	 * 
	 * @param patientId
	 * @return
	 */
	@RequestMapping(value = "/prepareNew", produces = "application/json")
	public @ResponseBody
	Visit prepareNewVisit(@ModelAttribute final Patient patient) {
		return visitService.prepareNewVisit(patient);
	}

	/**
	 * Usuwa wizytę z bazy
	 * 
	 * @param visitId
	 * @return
	 */
	@RequestMapping(value = "/{visitId}/delete", method = RequestMethod.GET)
	public String deleteVisit(@PathVariable final Long visitId,
			@ModelAttribute final Patient patient, final Model model) {
		final Visit visit = visitService.getVisitById(visitId);
		if (visit == null) {
			model.addAttribute("message", "Nie ma takiej wizyty.");
			return "error";
		}
		if (visit.getPatient() == null
				|| !patient.getPatientId().equals(
						visit.getPatient().getPatientId())) {
			model.addAttribute("message",
					"Wizyta nie należy do podanego pacjenta.");
			return "error";
		}
		visitService.removeVisit(visit);
		model.addAttribute("message", "Wizyta została usunięta.");
		return "alert";
	}

	/**
	 * Wyświetla stronę JSP z historią wizyt z perspektywy szczęki
	 * 
	 * @param patient
	 * @return
	 */
	@RequestMapping(value = "teethHistory")
	public String showTeethHistory(@ModelAttribute final Patient patient) {
		this.visitService.sortVisitsByDate(patient);
		return "teethHistory";
	}

	@RequestMapping(value = "/{visitId}/addAttachment", method = RequestMethod.POST)
	public String addAttachment(@PathVariable final Long visitId,
			final UploadItem uploadItem, final BindingResult result,
			final Model model) {

		if (uploadItem.getDescription().length() == 0) {
			model.addAttribute("message", "Opis wymagany.");
			model.addAttribute("uploadItem", uploadItem);
			return "visits";
		}
		if (uploadItem.getFileData().getSize() == 0) {
			model.addAttribute("message", "Plik jest pusty.");
			model.addAttribute("uploadItem", uploadItem);
			return "visits";
		}

		if (result.hasErrors()) {
			return "visits";
		}
		final Visit visit = visitService.getVisitById(visitId);
		if (visit == null) {
			model.addAttribute("message", "Nie ma takiej wizyty.");
			return "error";
		}
		final Attachment attachment = createAttachment(uploadItem, visit);
		attachmentService.save(attachment);
		return "redirect:/patients/{patientId}/visits";
	}

	@RequestMapping(value = "/{visitId}/attachments/{attachmentId}", method = RequestMethod.GET)
	public void getFile(@PathVariable("attachmentId") final Long attachmentId,
			final HttpServletResponse response) {
		try {
			final Attachment attachment = attachmentService
					.getById(attachmentId);
			response.setHeader("Content-Disposition", "attachment; filename="
					+ attachment.getFileName());
			response.getOutputStream().write(attachment.getFile());
			response.flushBuffer();
		} catch (final IOException ex) {
			throw new RuntimeException("IOError writing file to output stream");
		}

	}

	@RequestMapping(value = "/{visitId}/show", method = RequestMethod.GET)
	public String showVisit(@PathVariable("visitId") final Long visitId,
			final HttpServletResponse response, final Model model) {
		final List<ToothState> dict = (List<ToothState>) dictionaryService
				.getAllDictionaries().getToothStates();
		model.addAttribute("dictionary", dict);

		final Visit visit = visitService.getVisitById(visitId);
		model.addAttribute("visit", visit);
		return "visit";
	}

	private Attachment createAttachment(final UploadItem uploadItem,
			final Visit visit) {
		final Attachment attachment = new Attachment();
		attachment.setDescription(uploadItem.getDescription());
		attachment
				.setFileName(uploadItem.getFileData().getFileItem().getName());
		attachment.setFile(uploadItem.getFileData().getBytes());
		attachment.setVisit(visit);
		return attachment;
	}
}
