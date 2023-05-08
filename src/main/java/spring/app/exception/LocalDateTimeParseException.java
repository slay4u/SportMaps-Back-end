package spring.app.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class LocalDateTimeParseException extends JsonProcessingException {
    public LocalDateTimeParseException(String msg) {
        super(msg);
    }
}
