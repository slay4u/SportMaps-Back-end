package spring.app.modules.event.service;

import org.springframework.web.multipart.MultipartFile;
import spring.app.modules.event.dto.EventAllInfoDto;
import spring.app.modules.event.dto.EventCreateDto;

import java.io.IOException;
import java.util.List;

public interface EventService {
    int createEvent(EventCreateDto event);

    int updateEvent(Long id, EventCreateDto event);

    EventAllInfoDto getEventById(Long id);

    void deleteById(Long id);

    List<EventAllInfoDto> getAllEvents(int pageNumber);

    String uploadImage(MultipartFile file, Long id) throws IOException;

    byte[] downloadImages(Long id) throws IOException;

    double getTotalPagesCount();
}
