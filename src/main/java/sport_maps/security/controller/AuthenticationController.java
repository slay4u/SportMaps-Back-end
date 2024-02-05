package sport_maps.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import sport_maps.security.dto.LoginRequest;
import sport_maps.security.dto.RegisterRequest;
import org.springframework.web.bind.annotation.*;
import sport_maps.security.dto.AuthenticationResponse;
import sport_maps.security.service.AuthenticationService;
import sport_maps.security.service.RefreshTokenService;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static sport_maps.commons.BaseController.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationController(AuthenticationService authenticationService, RefreshTokenService refreshTokenService) {
        this.authenticationService = authenticationService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody RegisterRequest registerRequest) {
        authenticationService.signup(registerRequest);
    }

    @GetMapping("/{token}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void verifyAccount(@PathVariable("token") String token) {
        authenticationService.verifyToken(token);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthenticationResponse response = authenticationService.login(loginRequest);
        return ResponseEntity.ok().header(SET_COOKIE, response.userInfo().toString()).body(response.token());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.RESET_CONTENT)
    public String refresh(HttpServletRequest request) {
        return authenticationService.refreshToken(request);
    }

    @DeleteMapping
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return ResponseEntity.noContent().header(SET_COOKIE, refreshTokenService.deleteToken(request).toString()).build();
    }
}
