package sport_maps.coach.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import sport_maps.coach.dto.CoachDto;
import sport_maps.coach.dto.CoachSaveDto;
import sport_maps.coach.service.CoachService;
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

import java.io.IOException;

import static sport_maps.security.general.SecurityURLs.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/coaches")
public class CoachController {
    private final CoachService service;

    public CoachController(CoachService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCoach(@Valid @RequestBody CoachSaveDto requestToSave) {
        service.createCoach(requestToSave);
    }

    @GetMapping(params = {"page"})
    @ResponseStatus(HttpStatus.OK)
    public Page<CoachDto> getAllCoaches(@RequestParam("page") int page) {
        return service.getAllCoaches(page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CoachDto getCoachById(@PathVariable("id") Long id) {
        return service.getCoachById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateCoach(@PathVariable("id") Long id, @Valid @RequestBody CoachSaveDto requestToSave) {
        service.updateCoach(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoach(@PathVariable("id") Long id) {
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
