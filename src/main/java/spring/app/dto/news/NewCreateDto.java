package spring.app.dto.news;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;
import spring.app.util.LocalDateDeserializer;

import java.time.LocalDateTime;

@Getter
@Builder
@Jacksonized
public class NewCreateDto {
    @NotBlank(message = "Name can't be empty")
    private String name;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime publishDate;

    private String desc;
}
