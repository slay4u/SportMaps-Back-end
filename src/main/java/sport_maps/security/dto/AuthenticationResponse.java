package sport_maps.security.dto;

import org.springframework.http.ResponseCookie;

public record AuthenticationResponse(String token, ResponseCookie userInfo) {}
