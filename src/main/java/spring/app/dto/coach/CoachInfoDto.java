package spring.app.dto.coach;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
public class CoachInfoDto {
    private String result;
    private Long id;
}
