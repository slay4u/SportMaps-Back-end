package spring.app.modules.news.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;
import spring.app.modules.comments.newsComment.domain.NewsComment;

import java.util.List;

@Jacksonized
@Getter
@Setter
@Builder
public class NewAllInfoDto {
    private Long id;
    private String name;
    private String publishDate;
    private String desc;
    private byte[] image;
    private String emailUser;
    private List<NewsComment> commentList;
}
