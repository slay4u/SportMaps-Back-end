package spring.app.service;

import org.springframework.web.multipart.MultipartFile;
import spring.app.dto.coach.CoachAllInfoDto;
import spring.app.dto.coach.CoachCreateDto;
import spring.app.dto.coach.CoachInfoDto;

import java.io.IOException;
import java.util.List;

public interface CoachService {
    CoachInfoDto createCoach(CoachCreateDto coachDto);

    CoachInfoDto updateCoach(Long id, CoachCreateDto coachDto);

    CoachAllInfoDto getCoachById(Long id);

    void deleteById(Long id);

    List<CoachAllInfoDto> getAllCoaches(int pageNumber);

    String uploadImage(MultipartFile file, Long id) throws IOException;

    byte[] downloadImages(Long id) throws IOException;

    double getTotalPagesCount();
}
