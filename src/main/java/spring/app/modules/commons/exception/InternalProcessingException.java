package spring.app.modules.commons.exception;

public class InternalProcessingException extends RuntimeException {

    public InternalProcessingException(String msg) {
        super(msg);
    }

    public InternalProcessingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
