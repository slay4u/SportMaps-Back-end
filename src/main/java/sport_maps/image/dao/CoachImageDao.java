package sport_maps.image.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sport_maps.image.domain.CoachImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachImageDao extends ImageDao<CoachImage> {
    Optional<CoachImage> findByNameAndFilePath(String name, String filePath);

    @Query(value = "SELECT * FROM public.coach_image" +
            " WHERE id_coach = ?1", nativeQuery = true)
    List<CoachImage> findAllByCoachId(Long id);
}
