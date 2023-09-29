package spring.app.modules.coach.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.modules.coach.domain.ImageData;
import spring.app.modules.imgs.dao.BaseImageDataDao;

import java.util.List;

@Repository
public interface CoachImageDataDao extends BaseImageDataDao<ImageData, Long> {

    @Override
    @Query(value = "SELECT * FROM public.img_coach" +
            " WHERE id_coach = ?1", nativeQuery = true)
    List<ImageData> findAllByFK(Long id);
}
