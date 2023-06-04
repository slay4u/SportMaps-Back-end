package spring.app.modules.smap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Builder
@Jacksonized
public class SMapDto {
    @NotNull
    private float lat;
    @NotNull
    private float lng;
    @NotNull
    private int zoom;
    @NotBlank
    private String type;
}
