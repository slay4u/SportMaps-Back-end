package sport_maps.security.general;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class SecurityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseBody
    public ResponseEntity<RestError> handleInsufficientAuthenticationException(Exception e) {
        log.warn("InsufficientAuthenticationException thrown: {}", e.getMessage());
        RestError re = new RestError(HttpStatus.UNAUTHORIZED.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(re);
    }
}
