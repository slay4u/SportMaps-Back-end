package sport_maps.coach.service;

import org.springframework.web.multipart.MultipartFile;
import sport_maps.coach.dto.CoachDto;
import sport_maps.coach.dto.CoachSaveDto;

import java.io.IOException;
import java.util.List;

public interface CoachService {
    int createCoach(CoachSaveDto coachDto);
    int updateCoach(Long id, CoachSaveDto coachDto);
    CoachDto getCoachById(Long id);
    void deleteById(Long id);
    List<CoachDto> getAllCoaches(int pageNumber);
    String uploadImage(MultipartFile file, Long id) throws IOException;
    double getTotalPagesCount();
}
