package spring.app.modules.event.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
import spring.app.modules.commons.controller.BaseRestController;
import spring.app.modules.event.dto.EventAllInfoDto;
import spring.app.modules.event.dto.EventCreateDto;
import spring.app.modules.event.service.EventService;

import java.io.IOException;
import java.util.List;

import static spring.app.modules.commons.controller.BaseRestController.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "events")
@AllArgsConstructor
public class EventController extends BaseRestController {
    private final EventService eventService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public int createEvent(@Valid @RequestBody EventCreateDto requestToSave) {
        return eventService.createEvent(requestToSave);
    }

    @GetMapping(params = {"page_num"})
    @ResponseStatus(HttpStatus.OK)
    public List<EventAllInfoDto> getAllEvents(int page_num) {
        return eventService.getAllEvents(page_num);
    }

    @GetMapping("/byId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventAllInfoDto getEventById(@PathVariable("id") Long id) {
        return eventService.getEventById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int updateEventById(@Valid @PathVariable("id") Long id,
                                    @RequestBody EventCreateDto requestToSave) {
        return eventService.updateEvent(id, requestToSave);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEventById(@PathVariable("id") Long id) {
        eventService.deleteById(id);
    }

    @PostMapping("/photo/upload/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long id,
                                         @RequestParam ("image") MultipartFile file) throws IOException {
        String uploadImage = eventService.uploadImage(file, id);
        return ResponseEntity.ok(uploadImage);
    }

    @GetMapping(value = "/count")
    @ResponseStatus(HttpStatus.OK)
    public double getTotalPagesCount() {
        return eventService.getTotalPagesCount();
    }
}
