package sport_maps.nef.dao;

import org.springframework.stereotype.Repository;
import sport_maps.nef.domain.Forum;

import java.util.Optional;

@Repository
public interface ForumDao extends NEFDao<Forum> {
    Optional<Forum> findByName(String name);
}
