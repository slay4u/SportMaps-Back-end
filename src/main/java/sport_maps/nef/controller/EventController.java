package sport_maps.nef.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.image.service.ImageService;
import sport_maps.nef.dto.EventDto;
import sport_maps.nef.dto.EventSaveDto;
import sport_maps.nef.service.EventService;

import java.io.IOException;

import static sport_maps.security.general.SecurityURLs.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/events")
public class EventController {
    private final EventService service;
    @Qualifier("eventImageServiceImpl")
    private final ImageService imageService;

    public EventController(EventService service, @Qualifier("eventImageServiceImpl") ImageService imageService) {
        this.service = service;
        this.imageService = imageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@Valid @RequestPart("event") EventSaveDto requestToSave, @RequestPart MultipartFile image) throws IOException {
        service.createEvent(requestToSave);
        imageService.uploadImage(requestToSave.name(), image);
    }

    @GetMapping(params = {"page"})
    @ResponseStatus(HttpStatus.OK)
    public Page<EventDto> getAllEvents(@RequestParam("page") int page) {
        return service.getAllEvents(page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventDto getEventById(@PathVariable("id") Long id) {
        return service.getEventById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateEvent(@PathVariable("id") Long id, @Valid @RequestPart("event") EventSaveDto requestToSave,
                            @RequestPart(required = false) MultipartFile image) throws IOException {
        if (image != null) imageService.updateImage(id, image);
        service.updateEvent(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable("id") Long id) throws IOException {
        imageService.deleteAllImages(id);
        service.deleteById(id);
    }
}
