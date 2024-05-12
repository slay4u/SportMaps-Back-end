package sport_maps.coach.service;

import org.springframework.data.domain.Page;
import sport_maps.coach.dto.CoachDto;
import sport_maps.coach.dto.CoachSaveDto;

public interface CoachService {
    int size = 15;
    void createCoach(CoachSaveDto coachDto);
    void updateCoach(Long id, CoachSaveDto coachDto);
    CoachDto getCoachById(Long id);
    void deleteById(Long id);
    Page<CoachDto> getAllCoaches(int page);
}
