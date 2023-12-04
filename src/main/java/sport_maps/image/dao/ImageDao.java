package sport_maps.image.dao;

import org.springframework.data.repository.NoRepositoryBean;
import sport_maps.image.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

@NoRepositoryBean
public interface ImageDao<T extends Image> extends JpaRepository<T, Long> {

}
