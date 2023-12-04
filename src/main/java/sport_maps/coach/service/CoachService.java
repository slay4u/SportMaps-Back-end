package sport_maps.coach.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.coach.dto.CoachDto;
import sport_maps.coach.dto.CoachSaveDto;

import java.io.IOException;

public interface CoachService {
    int size = 15;
    void createCoach(CoachSaveDto coachDto);
    void updateCoach(Long id, CoachSaveDto coachDto);
    CoachDto getCoachById(Long id);
    void deleteById(Long id);
    Page<CoachDto> getAllCoaches(int page);
    String uploadImage(MultipartFile file, Long id) throws IOException;
}
