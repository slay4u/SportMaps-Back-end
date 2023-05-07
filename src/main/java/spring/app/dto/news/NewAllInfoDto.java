package spring.app.dto.news;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
@Setter
public class NewAllInfoDto {
    private Long id;
    private String name;
    private String publishDate;
    private String desc;
    private byte[] image;
}
