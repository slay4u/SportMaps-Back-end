package spring.app.modules.coach.service;

import org.springframework.stereotype.Service;
import spring.app.modules.coach.dao.CoachImageDataDao;
import spring.app.modules.coach.domain.ImageData;
import spring.app.modules.imgs.service.BaseImageDataService;

@Service
public class CoachImageDataService extends BaseImageDataService<ImageData, Long, CoachImageDataDao> {

    protected CoachImageDataService(CoachImageDataDao dao) {
        super(dao);
    }
}
