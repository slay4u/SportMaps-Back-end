package spring.app.modules.commons.other;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;
import spring.app.modules.commons.constant.SystemConstants;
import spring.app.modules.commons.io.LocalDateDeserializer;

import java.time.LocalDate;

@Getter
@Setter
@Jacksonized
public class PersonDto {
    @NotBlank(message = "First name is required")
    @Pattern(regexp = SystemConstants.FIRST_NAME_REGEXP)
    protected String firstName;
    @NotBlank(message = "Last name is required")
    @Pattern(regexp = SystemConstants.LAST_NAME_REGEXP)
    protected String lastName;
    // Optional
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    protected LocalDate dob;
}
