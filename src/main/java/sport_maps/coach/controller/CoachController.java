package sport_maps.coach.controller;

import jakarta.validation.Valid;
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
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static sport_maps.commons.BaseController.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/coaches")
public class CoachController {
    private final CoachService service;

    public CoachController(CoachService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(OK)
    public int createCoach(@Valid @RequestBody CoachSaveDto requestToSave) {
        return service.createCoach(requestToSave);
    }

    @GetMapping(params = {"page_num"})
    @ResponseStatus(OK)
    public List<CoachDto> getAllCoaches(int page_num) {
        return service.getAllCoaches(page_num);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public CoachDto getCoachById(@PathVariable("id") Long id) {
        return service.getCoachById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public int updateCoach(@PathVariable("id") Long id, @Valid @RequestBody CoachSaveDto requestToSave) {
        return service.updateCoach(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void deleteCoach(@PathVariable("id") Long id) {
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
