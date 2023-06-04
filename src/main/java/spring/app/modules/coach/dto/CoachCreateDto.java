package spring.app.modules.coach.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class CoachCreateDto {
    @NotBlank(message = "First name can't be empty")
    private String firstName;
    @NotBlank(message = "Last name can't be empty")
    private String lastName;
    @NotNull(message = "Age can't  be empty")
    private Long age;
    @NotNull(message = "Experience can't  be empty")
    private Long experience;
    @NotNull(message = "Price can't  be empty")
    private Double price;
    private String description;
    @NotNull(message = "SportType can't be empty")
    private String sportType;
}
