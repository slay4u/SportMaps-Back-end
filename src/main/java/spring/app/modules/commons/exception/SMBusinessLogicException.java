package spring.app.modules.commons.exception;

import spring.app.modules.commons.other.ErrorMap;

public class SMBusinessLogicException extends RuntimeException {

    private ErrorMap errors;

    public SMBusinessLogicException(String message) {
        super(message);
    }

    public SMBusinessLogicException(ErrorMap errors) {
        this.errors = errors;
    }

    public ErrorMap getErrors() {
        return errors;
    }
}
