package sport_maps.nef.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import sport_maps.nef.domain.NewsEventForum;

@NoRepositoryBean
public interface NEFDao<T extends NewsEventForum> extends JpaRepository<T, Long> {

}
