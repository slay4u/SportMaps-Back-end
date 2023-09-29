package spring.app.modules.coach.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import spring.app.modules.pd.st.SportType;
import spring.app.modules.commons.other.PersonDto;
import spring.app.modules.commons.util.convert.BaseDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Jacksonized
@Getter
@Setter
@BaseDto
public class CoachAllInfoDto extends PersonDto {
    // Seems to be fine with PersonDto.dob
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dob;
    private BigDecimal price;
    private String description;
    private Map<SportType, Long> sports;
    private byte[] image;
}
