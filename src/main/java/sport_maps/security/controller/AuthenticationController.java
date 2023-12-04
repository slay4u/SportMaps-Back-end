package sport_maps.security.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import sport_maps.security.dto.LoginRequest;
import sport_maps.security.dto.RegisterRequest;
import org.springframework.web.bind.annotation.*;
import sport_maps.security.dto.AuthenticationResponse;
import sport_maps.security.dto.RefreshTokenRequest;
import sport_maps.security.service.AuthenticationService;
import sport_maps.security.service.RefreshTokenService;

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
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshToken) {
        return authenticationService.refreshToken(refreshToken);
    }

    @DeleteMapping("/{token}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@PathVariable("token") String token) {
        refreshTokenService.deleteToken(token);
    }
}
