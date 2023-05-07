package spring.app.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class LocalDateParseException extends JsonProcessingException {
    public LocalDateParseException(String msg) {
        super(msg);
    }
}
