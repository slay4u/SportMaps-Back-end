package sport_maps.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sport_maps.security.dto.LoginRequest;
import sport_maps.security.dto.RegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sport_maps.security.domain.User;
import sport_maps.security.dto.AuthenticationResponse;
import sport_maps.security.dto.RefreshTokenRequest;
import sport_maps.security.service.AuthenticationService;
import sport_maps.security.service.RefreshTokenService;

import static sport_maps.commons.BaseController.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody RegisterRequest registerRequest) {
        String response = authenticationService.signup(registerRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token) {
        authenticationService.verifyToken(token);
        return new ResponseEntity<>("Account activated successfully!", HttpStatus.OK);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/refresh")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshToken) {
        return authenticationService.refreshToken(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshToken) {
        refreshTokenService.deleteRefreshToken(refreshToken.refreshToken());
        return ResponseEntity.status(HttpStatus.OK).body("Please, log in!");
    }

    @GetMapping("/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return authenticationService.getUserByEmail(email);
    }
}
