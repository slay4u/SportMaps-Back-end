package spring.app.modules.coach.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.modules.coach.domain.Coach;
import spring.app.modules.commons.dao.BaseDao;

@Repository
public interface CoachDao extends BaseDao<Coach, Long> {

    /*
     * TODO: Optimize (count is slow)
     */
    @Query(value = "SELECT COUNT(*) FROM public.coaches",
            nativeQuery = true)
    long countEverything();
}
