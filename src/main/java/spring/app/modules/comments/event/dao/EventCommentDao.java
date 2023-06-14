package spring.app.modules.comments.event.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.modules.comments.event.domain.EventComment;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventCommentDao extends JpaRepository<EventComment, Long> {
    @Query(value = "SELECT * FROM public.event_comments", nativeQuery = true)
    List<EventComment> getAllEventComments();

    @Query(value = "SELECT * FROM public.event_comments" +
            " WHERE id_event_comment = ?1", nativeQuery = true)
    Optional<EventComment> getEventCommentById(Long id);

    @Query(value = "SELECT * FROM public.event_comments" +
            " WHERE id_event = ?1", nativeQuery = true)
    List<EventComment> findAllByEventId(Long id);
}
