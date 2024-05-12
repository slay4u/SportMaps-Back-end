package sport_maps.nef.service;

import org.springframework.data.domain.Page;
import sport_maps.nef.dto.EventDto;
import sport_maps.nef.dto.EventSaveDto;

public interface EventService {
    int size = 15;
    void createEvent(EventSaveDto dto);
    void updateEvent(Long id, EventSaveDto dto);
    EventDto getEventById(Long id);
    void deleteById(Long id);
    Page<EventDto> getAllEvents(int page);
}
