package spring.app.modules.coach.service;

import org.springframework.web.multipart.MultipartFile;
import spring.app.modules.coach.dto.CoachAllInfoDto;
import spring.app.modules.coach.dto.CoachCreateDto;

import java.io.IOException;
import java.util.List;

public interface CoachService {
    int createCoach(CoachCreateDto coachDto);

    int updateCoach(Long id, CoachCreateDto coachDto);

    CoachAllInfoDto getCoachById(Long id);

    void deleteById(Long id);

    List<CoachAllInfoDto> getAllCoaches(int pageNumber);

    String uploadImage(MultipartFile file, Long id) throws IOException;

    double getTotalPagesCount();
}
