package sport_maps.nef.dao;

import sport_maps.nef.domain.News;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsDao extends NEFDao<News> {
    Optional<News> findByName(String name);
    boolean existsByName(String name);
}
