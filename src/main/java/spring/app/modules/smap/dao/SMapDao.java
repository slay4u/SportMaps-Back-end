package spring.app.modules.smap.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.app.modules.smap.domain.SMap;

@Repository
public interface SMapDao extends JpaRepository<SMap, Long> {

}
