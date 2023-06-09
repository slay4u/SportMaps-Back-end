package spring.app.modules.comments.newsComment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@Builder
public class NewsCommentDto {
    private Long id;
    private String createdDate;
    private String text;
    private String emailUser;
    private Long idNew;
}
