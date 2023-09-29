package spring.app.modules.coach.service;

import org.springframework.web.multipart.MultipartFile;
import spring.app.modules.coach.dto.CoachAllInfoDto;
import spring.app.modules.coach.dto.CoachCreateDto;
import spring.app.modules.commons.service.IBaseService;

import java.util.List;

public interface CoachService extends IBaseService {
    int createCoach(CoachCreateDto coachDto);

    int updateCoach(Long id, CoachCreateDto coachDto);

    CoachAllInfoDto getCoachById(Long id);

    void deleteById(Long id);

    List<CoachAllInfoDto> getAllCoaches(int pageNumber);

    String uploadImage(MultipartFile file, Long id);

    double getTotalPagesCount();
}
