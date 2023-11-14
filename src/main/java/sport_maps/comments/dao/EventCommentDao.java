package sport_maps.comments.dao;

import org.springframework.stereotype.Repository;
import sport_maps.comments.domain.EventComment;

@Repository
public interface EventCommentDao extends CommentDao<EventComment> {

}
