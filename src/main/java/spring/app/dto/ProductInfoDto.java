package spring.app.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
@Getter
public class ProductInfoDto {
    private String result;
    private Long id;
}
