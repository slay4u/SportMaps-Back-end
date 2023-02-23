package spring.app.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.app.config.JwtService;
import spring.app.domain.Role;
import spring.app.domain.User;
import spring.app.repository.UserDao;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserDao rep;
    private final PasswordEncoder passwordEncoder;
    private final JwtService service;
    private final AuthenticationManager manager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder().firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build();
        rep.save(user);
        var jwtToken = service.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = rep.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = service.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
