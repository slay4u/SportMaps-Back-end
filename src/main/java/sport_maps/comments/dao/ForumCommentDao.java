package sport_maps.comments.dao;

import org.springframework.stereotype.Repository;
import sport_maps.comments.domain.ForumComment;

@Repository
public interface ForumCommentDao extends CommentDao<ForumComment> {

}
