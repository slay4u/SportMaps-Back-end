package spring.app.dto.events;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
public class EventInfoDto {
    private String result;
    private Long id;
}
