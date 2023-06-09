package spring.app.modules.comments.newsComment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.modules.comments.newsComment.domain.NewsComment;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsCommentDao extends JpaRepository<NewsComment, Long> {
    @Query(value = "SELECT * FROM public.news_comments", nativeQuery = true)
    List<NewsComment> getAllNewsComments();

    @Query(value = "SELECT * FROM public.news_comments" +
            " WHERE id_news_comment = ?1", nativeQuery = true)
    Optional<NewsComment> getNewsCommentById(Long id);

    @Query(value = "SELECT * FROM public.news_comments" +
            " WHERE id_new = ?1", nativeQuery = true)
    List<NewsComment> findAllByNewsId(Long id);
}
