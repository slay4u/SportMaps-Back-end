package sport_maps.security.controller;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({IllegalArgumentException.class, EntityExistsException.class, IOException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void IllegalArgumentAndEntityExistsAndIOAndHttpMessageNotReadable(Exception e) {
        switch (e) {
            case IllegalArgumentException ignored -> log.warn("IllegalArgumentException thrown: {}", e.getMessage());
            case EntityExistsException ignored -> log.warn("EntityExistsException thrown: {}", e.getMessage());
            case IOException ignored -> log.warn("IOException thrown: {}", e.getMessage());
            case HttpMessageNotReadableException ignored -> log.warn("HttpMessageNotReadableException thrown: {}", e.getMessage());
            default -> {}
        }
    }

    @ExceptionHandler({EntityNotFoundException.class, UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void EntityNotFoundAndUsernameNotFound(Exception e) {
        switch (e) {
            case EntityNotFoundException ignored -> log.warn("EntityNotFoundException thrown: {}", e.getMessage());
            case UsernameNotFoundException ignored -> log.warn("UsernameNotFoundException thrown: {}", e.getMessage());
            default -> {}
        }
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void CredentialsExpired(Exception e) {
        log.warn("CredentialsExpiredException thrown: {}", e.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public void InsufficientAuthentication(Exception e) {
        log.warn("InsufficientAuthenticationException thrown: {}", e.getMessage());
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, UnsatisfiedServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public void HttpRequestMethodNotSupportedAndUnsatisfiedServletRequestParameter(Exception e) {
        switch (e) {
            case HttpRequestMethodNotSupportedException ignored -> log.warn("HttpRequestMethodNotSupportedException thrown: {}", e.getMessage());
            case UnsatisfiedServletRequestParameterException ignored -> log.warn("UnsatisfiedServletRequestParameterException thrown: {}", e.getMessage());
            default -> {}
        }
    }
}
