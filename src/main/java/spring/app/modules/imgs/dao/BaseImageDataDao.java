package spring.app.modules.imgs.dao;

import org.springframework.data.repository.NoRepositoryBean;
import spring.app.modules.commons.dao.BaseDao;

import java.util.List;

@NoRepositoryBean
public interface BaseImageDataDao <IMG, ID> extends BaseDao<IMG, ID> {

    /**
     * Must be overriden in derived subclasses
     * @return
     */
    List<IMG> findAllByFK(ID id);
}
