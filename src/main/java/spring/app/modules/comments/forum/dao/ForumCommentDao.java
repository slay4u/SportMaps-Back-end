package spring.app.modules.comments.forum.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.modules.comments.forum.domain.ForumComment;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForumCommentDao extends JpaRepository<ForumComment, Long> {
    @Query(value = "SELECT * FROM public.forum_comments", nativeQuery = true)
    List<ForumComment> getAllForumComments();

    @Query(value = "SELECT * FROM public.forum_comments" +
            " WHERE id_forum_comment = ?1", nativeQuery = true)
    Optional<ForumComment> getForumCommentById(Long id);

    @Query(value = "SELECT * FROM public.forum_comments" +
            " WHERE id_forum = ?1", nativeQuery = true)
    List<ForumComment> findAllByForumId(Long id);
}
