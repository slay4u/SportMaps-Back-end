package spring.app.service;

import org.springframework.web.multipart.MultipartFile;
import spring.app.dto.events.EventAllInfoDto;
import spring.app.dto.events.EventInfoDto;
import spring.app.dto.events.EventCreateDto;

import java.io.IOException;
import java.util.List;

public interface EventService {
    EventInfoDto createEvent(EventCreateDto event);

    EventInfoDto updateEvent(Long id, EventCreateDto event);

    EventAllInfoDto getEventById(Long id);

    void deleteById(Long id);

    List<EventAllInfoDto> getAllEvents(int pageNumber);

    String uploadImage(MultipartFile file, Long id) throws IOException;

    byte[] downloadImages(Long id) throws IOException;

    double getTotalPagesCount();
}
