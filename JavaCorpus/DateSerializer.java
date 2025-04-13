package pl.poznan.put.dentalsurgery.components;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.springframework.stereotype.Component;

@Component
public class DateSerializer extends JsonSerializer<Date> {

	private static final DateFormat FORMATTER = new SimpleDateFormat(
			"dd-MM-yyyy");

	@Override
	public void serialize(final Date value, final JsonGenerator jgen,
			final SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeString(FORMATTER.format(value));

	}

}
