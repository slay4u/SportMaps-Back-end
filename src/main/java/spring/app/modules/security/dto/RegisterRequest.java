package spring.app.modules.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.app.modules.commons.constant.SystemConstants;
import spring.app.modules.commons.other.PersonDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest extends PersonDto {
    @NotEmpty(message = "Email is required")
    @Email(regexp = SystemConstants.EMAIL_REGEXP)
    private String email;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = SystemConstants.PASSWORD_REGEXP, message = "Password not valid")
    private String password;
}
