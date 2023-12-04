package sport_maps.nef.service;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.nef.dto.EventDto;
import sport_maps.nef.dto.EventSaveDto;

import java.io.IOException;

public interface EventService {
    int size = 15;
    void createEvent(EventSaveDto dto);
    void updateEvent(Long id, EventSaveDto dto);
    EventDto getEventById(Long id);
    void deleteById(Long id);
    Page<EventDto> getAllEvents(int page);
    String uploadImage(MultipartFile file, Long id) throws IOException;
}
