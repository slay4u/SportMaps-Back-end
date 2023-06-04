package spring.app.modules.security.config.aspect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import spring.app.modules.commons.exception.AuthenticationException;
import spring.app.modules.security.service.AuthenticationService;
import spring.app.modules.security.service.UserDetailsServiceImpl;
import spring.app.modules.security.dto.RefreshTokenRequest;

import java.util.Arrays;

@Aspect
@Configuration
@Slf4j
@RequiredArgsConstructor
public class UserAccessAspect {

    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationService authenticationService;

    @Before("execution(* spring.app.service.impl.*.*(..))")
    public void checkAuth(JoinPoint joinPoint) {
        log.info("Check for user access...");
        User user = authenticationService.getCurrentUser()
                .orElseThrow(() -> new AuthenticationException("Unauthorized access!"));
        log.info("Request from user: {}", user.getUsername());
        log.info("Allowed execution for {}", joinPoint);
    }

    @Before("execution(* spring.app.modules.security.service.AuthenticationService.refreshToken(..))")
    public void checkUsernameRefreshToken(JoinPoint joinPoint) {
        User user = authenticationService.getCurrentUser().orElseThrow();
        Object[] signatureArgs = joinPoint.getArgs();
        RefreshTokenRequest rToken = (RefreshTokenRequest) Arrays.stream(signatureArgs)
                .findFirst()
                .orElseThrow();
        String emailToCheck = rToken.getEmail();
        spring.app.modules.security.domain.User userToCheck = userDetailsService
                .getUserByEmail(emailToCheck).orElseThrow(() ->
                        new AuthenticationException("User with email " + emailToCheck + " has not been found!"));
        compareUsers(user, userToCheck);

    }

    private void checkTwoUsers(JoinPoint joinPoint) {
        User user = authenticationService.getCurrentUser().orElseThrow();
        Object[] signatureArgs = joinPoint.getArgs();
        Long idToCheck = (Long) Arrays.stream(signatureArgs).findFirst().orElseThrow();
        spring.app.modules.security.domain.User userToCheck = getUserOrThrow(idToCheck);
        compareUsers(user, userToCheck);
    }

    private spring.app.modules.security.domain.User getUserOrThrow(Long idUser) {
        return userDetailsService.getUserById(idUser)
                .orElseThrow(() -> new AuthenticationException("User id not found " + idUser));
    }

    private void compareUsers(User user, spring.app.modules.security.domain.User userToCheck) {
        if(user.getUsername().equals(userToCheck.getUsername())) {
            log.info("Authorized " + user.getUsername());
        } else {
            log.warn("Authenticated user: " + user.getUsername() + "\n" +
                    "Trying to access: " + userToCheck.getUsername());
            throw new AuthenticationException("Trying to access another user's resource!");
        }
    }
}
