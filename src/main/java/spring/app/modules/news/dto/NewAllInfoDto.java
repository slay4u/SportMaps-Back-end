package spring.app.modules.news.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import spring.app.modules.commons.util.convert.BaseDto;

@Jacksonized
@Getter
@Setter
@BaseDto(exclude = "image")
@NoArgsConstructor
public class NewAllInfoDto {
    private String name;
    private String publishDate;
    private String description;
    private byte[] image;
}
