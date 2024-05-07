package sport_maps.nef.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.nef.dto.EventDto;
import sport_maps.nef.dto.EventSaveDto;
import sport_maps.nef.service.EventService;

import java.io.IOException;

import static sport_maps.security.general.SecurityURLs.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/events")
public class EventController {
    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@Valid @RequestBody EventSaveDto requestToSave) {
        service.createEvent(requestToSave);
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
    public void updateEvent(@PathVariable("id") Long id, @Valid @RequestBody EventSaveDto requestToSave) {
        service.updateEvent(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable("id") Long id) {
        service.deleteById(id);
    }

    @PostMapping("/upload/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long id,
                                         @RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = service.uploadImage(file, id);
        return ResponseEntity.ok(uploadImage);
    }
}
