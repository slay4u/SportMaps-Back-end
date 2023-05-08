package spring.app.dto.events;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
@Setter
public class EventAllInfoDto {
    private Long id;
    private String name;
    private String eventDate;
    private String desc;
    private String sportType;
    private byte[] image;
}
