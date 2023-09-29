package spring.app.modules.coach.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import spring.app.modules.pd.st.SportType;
import spring.app.modules.commons.other.PersonDto;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Jacksonized
public class CoachCreateDto extends PersonDto {
    @NotNull(message = "Price can't  be empty")
    private BigDecimal price;
    private String description;
    @NotNull(message = "SportTypes can't be empty")
    private Map<SportType, Long> sports;
}
