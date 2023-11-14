package sport_maps.comments.dao;

import sport_maps.comments.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommentDao<T extends Comment> extends JpaRepository<T, Long> {

}
