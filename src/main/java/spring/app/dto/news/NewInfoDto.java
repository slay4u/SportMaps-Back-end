package spring.app.dto.news;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
public class NewInfoDto {
    private String result;
    private Long id;
}
