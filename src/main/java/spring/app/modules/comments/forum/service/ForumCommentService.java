package spring.app.modules.comments.forum.service;

import spring.app.modules.comments.forum.dto.ForumCommentCreateDto;
import spring.app.modules.comments.forum.dto.ForumCommentDto;

import java.util.List;

public interface ForumCommentService {
    int createForumComment(ForumCommentCreateDto commentCreateDto);

    int updateForumComment(Long id, ForumCommentCreateDto commentCreateDto);

    ForumCommentDto getForumCommentById(Long id);

    void deleteById(Long id);

    List<ForumCommentDto> getAllForumComments();
}
