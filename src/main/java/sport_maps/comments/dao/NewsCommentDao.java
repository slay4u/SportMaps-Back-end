package sport_maps.comments.dao;

import sport_maps.comments.domain.NewsComment;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsCommentDao extends CommentDao<NewsComment> {

}
