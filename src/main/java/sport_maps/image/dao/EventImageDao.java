package sport_maps.image.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sport_maps.image.domain.EventImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventImageDao extends ImageDao<EventImage> {
    Optional<EventImage> findByNameAndFilePath(String name, String filePath);

    @Query(value = "SELECT * FROM public.event_image" +
            " WHERE id_event = ?1", nativeQuery = true)
    List<EventImage> findAllByEventId(Long id);
}
