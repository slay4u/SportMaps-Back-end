package sport_maps.smap.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sport_maps.smap.domain.SMap;

@Repository
public interface SMapDao extends JpaRepository<SMap, Long> {

}
