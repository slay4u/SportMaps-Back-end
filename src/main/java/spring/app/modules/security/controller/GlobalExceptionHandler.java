package spring.app.modules.security.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.app.modules.commons.exception.AlreadyExistException;
import spring.app.modules.commons.exception.AuthenticationException;
import spring.app.modules.commons.exception.LocalDateTimeParseException;
import spring.app.modules.commons.exception.NotFoundException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Exception thrown: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        log.warn("NotFoundException thrown: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(LocalDateTimeParseException.class)
    protected ResponseEntity<Object> handleLocalDateParseException(LocalDateTimeParseException e) {
        log.warn("LocalDateTimeParseException thrown: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    protected ResponseEntity<Object> handleAlreadyExistException(AlreadyExistException e) {
        log.warn("AlreadyExistException thrown: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler({AuthenticationException.class})
    protected ResponseEntity<Object> handleAuthenticationException(AuthenticationException e) {
        log.warn("Exception thrown: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), FORBIDDEN);
    }

    private static ResponseEntity<Object> buildErrorResponse(String message, HttpStatus httpStatus) {
        var response = new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message);
        return ResponseEntity.status(httpStatus.value()).body(response);
    }

    @Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    static class ErrorResponse {
        private int status;
        private String error;
        private String message;
    }
}
