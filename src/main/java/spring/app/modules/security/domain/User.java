package spring.app.modules.security.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.app.modules.commons.constant.SystemConstants;

import java.time.Instant;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false, unique = true)
    @Email(regexp = SystemConstants.EMAIL_REGEXP)
    private String email;
    private Instant created;
    private boolean enabled;
    @NotNull
    private Role role;

    public String getUsername() {
        return email;
    }
}
