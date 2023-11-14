package sport_maps.nef.service;

import org.springframework.web.multipart.MultipartFile;
import sport_maps.nef.dto.EventDto;
import sport_maps.nef.dto.EventSaveDto;

import java.io.IOException;
import java.util.List;

public interface EventService {
    int createEvent(EventSaveDto dto);
    int updateEvent(Long id, EventSaveDto dto);
    EventDto getEventById(Long id);
    void deleteById(Long id);
    List<EventDto> getAllEvents(int pageNumber);
    String uploadImage(MultipartFile file, Long id) throws IOException;
    double getTotalPagesCount();
}
