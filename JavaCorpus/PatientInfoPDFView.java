package pl.poznan.put.dentalsurgery.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import pl.poznan.put.dentalsurgery.model.Illness;
import pl.poznan.put.dentalsurgery.model.Medication;
import pl.poznan.put.dentalsurgery.model.Patient;
import pl.poznan.put.dentalsurgery.model.PhoneNumber;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component
public class PatientInfoPDFView extends AbstractPdfView {
	private BaseFont baseFont;
	
	private Font cellDataFont;
	private Font cellDataFontBold;
	private Font headerFont;
	

	private static final DateFormat FORMATTER = new SimpleDateFormat(
			"dd-MM-yyyy");
	
	public PatientInfoPDFView() {
		try {
			this.baseFont = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1250, BaseFont.EMBEDDED);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.cellDataFont = new Font(baseFont, 12, Font.NORMAL);
		this.cellDataFontBold = new Font(baseFont, 12, Font.BOLD);
		this.headerFont =  new Font(baseFont, 20, Font.BOLD);
	}
	
	/**
	 * Buduje dokument PDF na podstawie danych pacjenta
	 */
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document,
			PdfWriter pdfWriter, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Patient patient = (Patient) model.get("patient");
		
        document.addTitle("Dane pacjenta: " + patient.getName() + " " + patient.getSurname());
          
	
		PdfPTable table = new PdfPTable(new float[]{.3f,.7f});
		
		table.addCell(new Phrase("Imię i nazwisko: ", cellDataFont));
		table.addCell(new Phrase(
				patient.getName() + " " + patient.getSurname(), cellDataFontBold));

		table.addCell(new Phrase("PESEL: ", cellDataFont));
		table.addCell(new Phrase(patient.getPesel(), cellDataFontBold));

		table.addCell(new Phrase("Płeć: ", cellDataFont));
		table.addCell(new Phrase(patient.getGender() == 'M'? "Mężczyzna": "Kobieta", cellDataFontBold));

		table.addCell(new Phrase("Data urodzenia: ", cellDataFont));
		table.addCell(new Phrase(FORMATTER.format(patient.getBornDate()), cellDataFontBold));
		
		table.addCell(new Phrase("Adres zamieszkania: ", cellDataFont));
		table.addCell(new Phrase(patient.getStreet()+"\n"+patient.getCity(), cellDataFontBold));
		
		StringBuilder phoneNumbers = new StringBuilder();
		for (PhoneNumber phoneNumber : patient.getPhoneNumbers()) {
			if (phoneNumber != null && phoneNumber.getNumber() != null)
				phoneNumbers.append(phoneNumber.getNumber()).append("\n");
		}
		
		table.addCell(new Phrase("Numer telefonu: ", cellDataFont));
		table.addCell(new Phrase(phoneNumbers.toString(), cellDataFontBold));
		
		StringBuilder illnesses = new StringBuilder();
		for (Illness illness : patient.getIllnesses()) {
			if (illness != null && illness.getName() != null) {
				illnesses.append(illness.getName()).append("\n");
			}
		}

		table.addCell(new Phrase("Choroby pacjenta: ", cellDataFont));
		table.addCell(new Phrase(illnesses.toString(), cellDataFontBold));
		
		StringBuilder medications = new StringBuilder();
		for (Medication med : patient.getMedications()) {
			if (med != null && med.getName() != null) {
				medications.append(med.getName()).append("\n");
			}
		}
		table.addCell(new Phrase("Przyjmowane leki: ", cellDataFont));
		table.addCell(new Phrase(medications.toString(), cellDataFontBold));
		
		
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Dane pacjenta: " + patient.getName() + " "
				+ patient.getSurname()));
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(Chunk.NEWLINE);
		document.add(table);
		
        
	}

}
