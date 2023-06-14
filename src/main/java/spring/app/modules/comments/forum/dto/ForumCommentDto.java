package spring.app.modules.comments.forum.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@Builder
public class ForumCommentDto {
    private Long id;
    private String createdDate;
    private String text;
    private String emailUser;
    private Long idForum;
}
