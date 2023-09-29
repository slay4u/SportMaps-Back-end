package spring.app.modules.commons.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class SerializationParseException extends JsonProcessingException {
    public SerializationParseException(String msg) {
        super(msg);
    }
}
