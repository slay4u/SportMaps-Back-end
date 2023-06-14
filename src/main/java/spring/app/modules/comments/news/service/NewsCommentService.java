package spring.app.modules.comments.news.service;

import spring.app.modules.comments.news.dto.NewsCommentCreateDto;
import spring.app.modules.comments.news.dto.NewsCommentDto;

import java.util.List;

public interface NewsCommentService {
    int createNewsComment(NewsCommentCreateDto commentDto);

    int updateNewsComment(Long id, NewsCommentCreateDto newsComment);

    NewsCommentDto getNewsCommentById(Long id);

    void deleteById(Long id);

    List<NewsCommentDto> getAllNewsComments();
}
