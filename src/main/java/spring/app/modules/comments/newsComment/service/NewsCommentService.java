package spring.app.modules.comments.newsComment.service;

import spring.app.modules.comments.newsComment.dto.NewsCommentCreateDto;
import spring.app.modules.comments.newsComment.dto.NewsCommentDto;

import java.util.List;

public interface NewsCommentService {
    int createNewsComment(NewsCommentCreateDto commentDto);

    int updateNewsComment(Long id, NewsCommentCreateDto newsComment);

    NewsCommentDto getNewsCommentById(Long id);

    void deleteById(Long id);

    List<NewsCommentDto> getAllNewsComments();
}
