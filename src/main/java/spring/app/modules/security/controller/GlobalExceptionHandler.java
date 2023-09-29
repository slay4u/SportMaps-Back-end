package spring.app.modules.security.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.app.modules.commons.exception.*;
import spring.app.modules.commons.other.ErrorMap;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        LOG(e, ex -> ex.getMessage() != null, ex -> log.error("Exception thrown: {}", ex.getMessage()), ex -> {});
        return buildErrorResponse(e.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        LOG(e, ex -> ex.getMessage() != null, ex -> log.error("NotFoundException thrown: {}", ex.getMessage()), ex -> {});
        return buildErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(SerializationParseException.class)
    protected ResponseEntity<Object> handleLocalDateParseException(SerializationParseException e) {
        LOG(e, ex -> ex.getMessage() != null, ex -> log.error("SerializationParseException thrown: {}", ex.getMessage()), ex -> {});
        return buildErrorResponse(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    protected ResponseEntity<Object> handleAlreadyExistException(AlreadyExistException e) {
        LOG(e, ex -> ex.getMessage() != null, ex -> log.error("AlreadyExistException thrown: {}", ex.getMessage()), ex -> {});
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(SMBusinessLogicException.class)
    protected ResponseEntity<?> handleSMBusinessLogicException(SMBusinessLogicException e) {
        LOG(e, ex -> ex.getMessage() != null, ex -> log.error("SMBusinessLogicException thrown: {}", ex.getMessage()), ex -> log.error("SMBusinessException thrown: {}", ((SMBusinessLogicException) ex).getErrors().getErrors()));
        if (!e.getErrors().isEmpty()) {
            return buildErrorResponse(e.getErrors().getErrors(), BAD_REQUEST);
        }
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException e) {
        LOG(e, ex -> ex.getMessage() != null, ex -> log.error("AuthenticationException thrown: {}", ex.getMessage()), ex -> {});
        return buildErrorResponse(e.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(InternalProcessingException.class)
    protected ResponseEntity<Object> handleInternalProcessingException(InternalProcessingException e) {
        LOG(e, ex -> ex.getMessage() != null, ex -> log.error("InternalProcessingException thrown: {}", ex.getMessage(), ex.getCause()), ex -> log.error("Internal processing exception thrown: ", ex.getCause()));
        return buildErrorResponse(e.getMessage(), FORBIDDEN);
    }

    private void stacktrace(Exception e) {
        log.error("Stacktrace: " + Arrays.toString(e.getStackTrace()));
    }

    // TODO: Distinct logger class
    private void LOG(Exception e, Predicate<Exception> condition, Consumer<Exception> initial, Consumer<Exception> other) {
        if(condition.test(e)) {
            initial.accept(e);
        } else {
            other.accept(e);
        }
        stacktrace(e);
    }

    private static ResponseEntity<Object> buildErrorResponse(String message, HttpStatus httpStatus) {
        var response = new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message);
        return ResponseEntity.status(httpStatus.value()).body(response);
    }

    private static ResponseEntity<Object> buildErrorResponse(Map<String, ErrorMap.Error> errors, HttpStatus httpStatus) {
        var response = new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), errors);
        return ResponseEntity.status(httpStatus.value()).body(response);
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ErrorResponse {
        private final int status;
        private final String error;
        private String message;
        private Map<String, ErrorMap.Error> errors;

        public ErrorResponse(int status, String error, String message) {
            this.status = status;
            this.error = error;
            this.message = message;
        }

        public ErrorResponse(int status, String error, Map<String, ErrorMap.Error> errors) {
            this.status = status;
            this.error = error;
            this.errors = errors;
        }
    }
}
