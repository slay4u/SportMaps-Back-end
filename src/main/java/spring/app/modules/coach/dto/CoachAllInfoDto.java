package spring.app.modules.coach.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Getter
@Setter
@Builder
public class CoachAllInfoDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Long age;
    private Long experience;
    private Double price;
    private String description;
    private String sportType;
    private byte[] image;
}
