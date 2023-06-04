package spring.app.modules.commons.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class LocalDateTimeParseException extends JsonProcessingException {
    public LocalDateTimeParseException(String msg) {
        super(msg);
    }
}
