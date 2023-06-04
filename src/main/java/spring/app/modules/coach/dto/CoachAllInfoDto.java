package spring.app.modules.coach.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import spring.app.modules.commons.util.convert.BaseDto;

@Jacksonized
@Getter
@Setter
@NoArgsConstructor
@BaseDto(exclude = {"image"})
public class CoachAllInfoDto {
    private String firstName;
    private String lastName;
    private Long age;
    private Long experience;
    private Double price;
    private String description;
    private String sportType;
    private byte[] image;
}
