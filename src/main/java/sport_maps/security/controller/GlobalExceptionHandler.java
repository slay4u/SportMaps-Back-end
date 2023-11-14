package sport_maps.security.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.time.format.DateTimeParseException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException thrown: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), FORBIDDEN);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("EntityNotFoundException thrown: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    protected ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException e) {
        log.warn("DateTimeParseException thrown: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<Object> handleEntityExistsException(EntityExistsException e) {
        log.warn("EntityExistsException thrown: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<Object> handleIOException(IOException e) {
        log.warn("IOException thrown: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler({CredentialsExpiredException.class, ExpiredJwtException.class})
    protected ResponseEntity<Object> handleAuthenticationException(CredentialsExpiredException e) {
        log.warn("CredentialsExpiredException thrown: {}", e.getMessage());
        return buildErrorResponse(e.getMessage(), FORBIDDEN);
    }

    private static ResponseEntity<Object> buildErrorResponse(String message, HttpStatus httpStatus) {
        var response = new ErrorResponse(httpStatus.value(), httpStatus.getReasonPhrase(), message);
        return ResponseEntity.status(httpStatus.value()).body(response);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record ErrorResponse(int status, String error, String message) {
    }
}
