package spring.app.modules.forum.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.springframework.format.annotation.DateTimeFormat;
import spring.app.modules.commons.util.LocalDateTimeDeserializer;

import java.time.LocalDateTime;

@Getter
@Builder
@Jacksonized
public class ForumCreateDto {
    @NotBlank(message = "Name can't be empty")
    private String name;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime createDate;
    @NotBlank(message = "Description can't be empty")
    private String desc;
}
