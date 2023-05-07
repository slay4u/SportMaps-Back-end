package spring.app.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.app.dto.events.EventAllInfoDto;
import spring.app.dto.events.EventCreateDto;
import spring.app.dto.events.EventInfoDto;
import spring.app.service.EventService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/sport-maps/v1/events")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public EventInfoDto createEvent(@Valid @RequestBody EventCreateDto requestToSave) {
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
    public EventInfoDto updateEventById(@Valid @PathVariable("id") Long id,
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

    @GetMapping(value = "/photo/download/{id}", produces="application/zip")
    @ResponseStatus(HttpStatus.OK)
    public byte[] downloadImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.addHeader("Content-Disposition", "attachment; filename=\"assigned_imgs.zip\"");
        return eventService.downloadImages(id);
    }

    @GetMapping(value = "/count")
    @ResponseStatus(HttpStatus.OK)
    public double getTotalPagesCount() {
        return eventService.getTotalPagesCount();
    }
}
