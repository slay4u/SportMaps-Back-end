package spring.app.modules.commons.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseDao<ENTITY, ID> extends JpaRepository<ENTITY, ID> {

}
