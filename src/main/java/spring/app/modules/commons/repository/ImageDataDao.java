package spring.app.modules.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.modules.commons.domain.ImageData;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageDataDao extends JpaRepository<ImageData, Long> {
    @Query(value = "SELECT * FROM public.image_data" +
            " WHERE name = ?1 AND file_path = ?2", nativeQuery = true)
    Optional<ImageData> findByNameAndFilePath(String name, String filePath);

    @Query(value = "SELECT * FROM public.image_data" +
            " WHERE id_new = ?1", nativeQuery = true)
    List<ImageData> findAllByANewId(Long id);

    @Query(value = "SELECT * FROM public.image_data" +
            " WHERE id_event = ?1", nativeQuery = true)
    List<ImageData> findAllByEventId(Long id);

    @Query(value = "SELECT * FROM public.image_data" +
            " WHERE id_coach = ?1", nativeQuery = true)
    List<ImageData> findAllByCoachId(Long id);
}
