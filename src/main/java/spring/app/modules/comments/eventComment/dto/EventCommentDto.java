package spring.app.modules.comments.eventComment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Setter
@Jacksonized
@Builder
public class EventCommentDto {
    private Long id;
    private String createdDate;
    private String text;
    private String emailUser;
    private Long idEvent;
}
