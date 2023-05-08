package spring.app.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import spring.app.exception.LocalDateTimeParseException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

    public LocalDateTimeDeserializer() {
        this(null);
    }

    public LocalDateTimeDeserializer(Class<?> vc) {
        super(vc);
    }

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext context)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        LocalDateTime parsedLocalDateTime;
        try {
            parsedLocalDateTime = LocalDateTime.parse(node.asText(), dateFormat);
        } catch (DateTimeParseException e) {
            throw new LocalDateTimeParseException("LocalDate field deserialization failed! Either empty or invalid pattern!");
        }
        return parsedLocalDateTime;
    }
}
