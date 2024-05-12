package sport_maps.nef.dao;

import sport_maps.nef.domain.Event;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventDao extends NEFDao<Event> {
    boolean existsByName(String name);
    Optional<Event> findByName(String name);
}
