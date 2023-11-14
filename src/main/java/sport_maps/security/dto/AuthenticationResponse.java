package sport_maps.security.dto;

public record AuthenticationResponse(
        String authenticationToken, Long idUser, String firstName, String lastName,
        String role, String expiresAt, String refreshToken) {
}
