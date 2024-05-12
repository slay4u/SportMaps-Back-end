package sport_maps.coach.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sport_maps.coach.dto.CoachDto;
import sport_maps.coach.dto.CoachSaveDto;
import sport_maps.coach.service.CoachService;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.image.service.ImageService;

import java.io.IOException;

import static sport_maps.security.general.SecurityURLs.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/coaches")
public class CoachController {
    private final CoachService service;
    @Qualifier("coachImageServiceImpl")
    private final ImageService imageService;

    public CoachController(CoachService service, @Qualifier("coachImageServiceImpl") ImageService imageService) {
        this.service = service;
        this.imageService = imageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCoach(@Valid @RequestPart("coach") CoachSaveDto requestToSave, @RequestPart MultipartFile image) throws IOException {
        service.createCoach(requestToSave);
        imageService.uploadImage(requestToSave.firstName() + ":" + requestToSave.lastName(), image);
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
    public void updateCoach(@PathVariable("id") Long id, @Valid @RequestPart("coach") CoachSaveDto requestToSave,
                            @RequestPart(required = false) MultipartFile image) throws IOException {
        if (image != null) imageService.updateImage(id, image);
        service.updateCoach(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCoach(@PathVariable("id") Long id) throws IOException {
        imageService.deleteAllImages(id);
        service.deleteById(id);
    }
}
