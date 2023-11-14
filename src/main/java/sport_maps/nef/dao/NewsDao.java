package sport_maps.nef.dao;

import sport_maps.nef.domain.News;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsDao extends NEFDao<News> {
    Optional<News> findNewsByName(String name);

    @Query(value = "SELECT COUNT(*) FROM public.news",
            nativeQuery = true)
    long getAllNewCount();
}
