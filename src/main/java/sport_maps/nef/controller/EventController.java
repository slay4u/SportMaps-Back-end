package sport_maps.nef.controller;

import jakarta.validation.Valid;
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
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static sport_maps.commons.BaseController.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/events")
public class EventController {
    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(OK)
    public int createEvent(@Valid @RequestBody EventSaveDto requestToSave) {
        return service.createEvent(requestToSave);
    }

    @GetMapping(params = {"page_num"})
    @ResponseStatus(OK)
    public List<EventDto> getAllEvents(int page_num) {
        return service.getAllEvents(page_num);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public EventDto getEventById(@PathVariable("id") Long id) {
        return service.getEventById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public int updateEvent(@PathVariable("id") Long id, @Valid @RequestBody EventSaveDto requestToSave) {
        return service.updateEvent(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void deleteEvent(@PathVariable("id") Long id) {
        service.deleteById(id);
    }

    @PostMapping("/upload/{id}")
    @ResponseStatus(OK)
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long id,
                                         @RequestParam ("image") MultipartFile file) throws IOException {
        String uploadImage = service.uploadImage(file, id);
        return ResponseEntity.ok(uploadImage);
    }

    @GetMapping("/count")
    @ResponseStatus(OK)
    public double getTotalPagesCount() {
        return service.getTotalPagesCount();
    }
}
