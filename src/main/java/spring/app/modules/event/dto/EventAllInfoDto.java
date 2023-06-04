package spring.app.modules.event.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import spring.app.modules.commons.util.convert.ConvertType;
import spring.app.modules.commons.util.convert.Dto;

@Jacksonized
@Getter
@Setter
@NoArgsConstructor
public class EventAllInfoDto {
    @Dto
    private String name;
    @Dto
    private String eventDate;
    @Dto(property = "description")
    private String desc;
    @Dto(value = ConvertType.ENUM)
    private String sportType;
    private byte[] image;
}
