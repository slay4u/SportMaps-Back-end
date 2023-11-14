package sport_maps.nef.dao;

import sport_maps.nef.domain.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventDao extends NEFDao<Event> {
    Optional<Event> findEventByName(String name);

    @Query(value = "SELECT COUNT(*) FROM public.event",
            nativeQuery = true)
    long getAllEventCount();
}
