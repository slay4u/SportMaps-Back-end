package spring.app.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String authenticationToken;
    private Long idUser;
    private String firstName;
    private String lastName;
    private String role;
    private String expiresAt;
    private String refreshToken;
}
