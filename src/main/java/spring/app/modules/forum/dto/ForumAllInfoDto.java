package spring.app.modules.forum.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import spring.app.modules.comments.forumComment.domain.ForumComment;

import java.util.List;

@Jacksonized
@Getter
@Setter
@Builder
public class ForumAllInfoDto {
    private Long id;
    private String name;
    private String createDate;
    private String desc;
    private String emailUser;
    private List<ForumComment> commentList;
}
