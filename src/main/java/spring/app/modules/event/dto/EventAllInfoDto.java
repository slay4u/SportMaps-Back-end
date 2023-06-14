package spring.app.modules.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import spring.app.modules.comments.event.domain.EventComment;

import java.util.List;

@Jacksonized
@Getter
@Setter
@Builder
public class EventAllInfoDto {
    private Long id;
    private String name;
    private String eventDate;
    private String desc;
    private String sportType;
    private byte[] image;
    private String emailUser;
    private List<EventComment> eventCommentList;
}
