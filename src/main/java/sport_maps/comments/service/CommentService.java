package sport_maps.comments.service;

import sport_maps.comments.dto.CommentSaveDto;

public interface CommentService {
    void createComment(CommentSaveDto dto);
    void updateComment(Long id, CommentSaveDto dto);
    void deleteById(Long id);
}
