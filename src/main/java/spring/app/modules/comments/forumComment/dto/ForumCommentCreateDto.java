package spring.app.modules.comments.forumComment.dto;

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
public class ForumCommentCreateDto {
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime createdDate;
    @NotBlank(message = "Comment can't be empty")
    private String text;
    @NotBlank(message = "Email can't be empty")
    private String emailUser;
    private Long idForum;
}
