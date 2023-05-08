package spring.app.dto.coach;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
@Setter
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
