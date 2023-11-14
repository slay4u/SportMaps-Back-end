package sport_maps.commons.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sport_maps.commons.domain.NewsImage;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsImageDao extends ImageDao<NewsImage> {
    Optional<NewsImage> findNewsImageByNameAndFilePath(String name, String filePath);

    @Query(value = "SELECT * FROM public.news_image" +
            " WHERE id_news = ?1", nativeQuery = true)
    List<NewsImage> findAllByNewsId(Long id);
}
