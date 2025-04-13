package pl.poznan.put.dentalsurgery.components;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.springframework.stereotype.Component;

import pl.poznan.put.dentalsurgery.web.VisitController;
 
@Component
public class DateDeserializer extends JsonDeserializer<Date> {
	private static final Log logger = LogFactory.getLog(DateDeserializer.class);

	private static final DateFormat FORMATTER = new SimpleDateFormat(
			"dd-MM-yyyy");

	@Override
	public Date deserialize(JsonParser jsonparser,
			DeserializationContext context) throws IOException,
			JsonProcessingException {
		String date = jsonparser.getText();
		try {
			return FORMATTER.parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
