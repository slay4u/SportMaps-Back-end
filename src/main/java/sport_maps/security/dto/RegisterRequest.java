package sport_maps.security.dto;

public record RegisterRequest(String firstName, String lastName, String email, String password) {
}
