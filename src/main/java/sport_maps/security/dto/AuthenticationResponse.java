package sport_maps.security.dto;

public record AuthenticationResponse(String token, String username, String role, String refreshToken) {}
