package sport_maps.nef.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sport_maps.nef.domain.Forum;

import java.util.Optional;

@Repository
public interface ForumDao extends NEFDao<Forum> {
    Optional<Forum> findForumByName(String name);

    @Query(value = "SELECT COUNT(*) FROM public.forum",
            nativeQuery = true)
    long getAllForumCount();
}
