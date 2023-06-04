package spring.app.modules.event.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.modules.event.domain.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventDao extends JpaRepository<Event, Long> {
    @Query(value = "SELECT * FROM public.events", nativeQuery = true)
    List<Event> getAllEvents(Pageable pageable);

    @Query(value = "SELECT * FROM public.events" +
            " WHERE name=?1", nativeQuery = true)
    Optional<Event> getEventByName(String name);

    @Query(value = "SELECT * FROM public.events" +
            " WHERE id_event = ?1", nativeQuery = true)
    Optional<Event> getEventById(Long id);

    @Query(value = "SELECT COUNT(*) FROM public.events",
            nativeQuery = true)
    long getAllEventCount();
}
