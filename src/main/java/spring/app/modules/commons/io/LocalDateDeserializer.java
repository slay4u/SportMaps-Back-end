package spring.app.modules.commons.io;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import spring.app.modules.commons.exception.SerializationParseException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// TODO: refactor
public class LocalDateDeserializer extends StdDeserializer<LocalDate> {

    public LocalDateDeserializer() {
        this(null);
    }

    public LocalDateDeserializer(Class<?> vc) {
        super(vc);
    }

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public LocalDate deserialize(JsonParser jp, DeserializationContext context)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        LocalDate parsedLocalDate;
        try {
            parsedLocalDate = LocalDate.parse(node.asText(), dateFormat);
        } catch (DateTimeParseException e) {
            throw new SerializationParseException("LocalDate field deserialization failed! Either empty or invalid pattern!");
        }
        return parsedLocalDate;
    }
}
