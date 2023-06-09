package spring.app.modules.forum.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.modules.forum.domain.Forum;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForumDao extends JpaRepository<Forum, Long> {
    @Query(value = "SELECT * FROM public.forums", nativeQuery = true)
    List<Forum> getAllForums(Pageable pageable);

    @Query(value = "SELECT * FROM public.forums" +
            " WHERE name=?1", nativeQuery = true)
    Optional<Forum> getForumByName(String name);

    @Query(value = "SELECT * FROM public.forums" +
            " WHERE id_forum = ?1", nativeQuery = true)
    Optional<Forum> getForumById(Long id);

    @Query(value = "SELECT COUNT(*) FROM public.forums",
            nativeQuery = true)
    long getAllForumCount();
}
