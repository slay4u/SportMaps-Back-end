package sport_maps.comments.service;

import sport_maps.comments.dto.CommentDto;
import sport_maps.comments.dto.CommentSaveDto;

import java.util.List;

public interface CommentService {
    int createComment(CommentSaveDto dto);
    int updateComment(Long id, CommentSaveDto dto);
    CommentDto getCommentById(Long id);
    List<CommentDto> getAllComments();
    void deleteById(Long id);
}
