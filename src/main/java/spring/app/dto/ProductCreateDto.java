package spring.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class ProductCreateDto {
    @NotNull(message = "Name can't be empty!")
    private String nameProduct;

    @NotNull(message = "Cost can't be empty!")
    private Double cost;
}
