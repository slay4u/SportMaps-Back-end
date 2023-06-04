package spring.app.modules.coach.controller;

import jakarta.servlet.http.HttpServletResponse;
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
import spring.app.modules.coach.dto.CoachAllInfoDto;
import spring.app.modules.coach.dto.CoachCreateDto;
import spring.app.modules.coach.service.CoachService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/sport-maps/v1/coaches")
@AllArgsConstructor
public class CoachController {
    private final CoachService coachService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public int createCoach(@Valid @RequestBody CoachCreateDto requestToSave) {
        return coachService.createCoach(requestToSave);
    }

    @GetMapping(params = {"page_num"})
    @ResponseStatus(HttpStatus.OK)
    public List<CoachAllInfoDto> getAllCoaches(int page_num) {
        return coachService.getAllCoaches(page_num);
    }

    @GetMapping("/byId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CoachAllInfoDto getCoachById(@PathVariable("id") Long id) {
        return coachService.getCoachById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int updateCoachById(@Valid @PathVariable("id") Long id,
                                        @RequestBody CoachCreateDto requestToSave) {
        return coachService.updateCoach(id, requestToSave);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteCoachById(@PathVariable("id") Long id) {
        coachService.deleteById(id);
    }

    @PostMapping("/photo/upload/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long id,
                                         @RequestParam ("image") MultipartFile file) throws IOException {
        String uploadImage = coachService.uploadImage(file, id);
        return ResponseEntity.ok(uploadImage);
    }

    @GetMapping(value = "/photo/download/{id}", produces="application/zip")
    @ResponseStatus(HttpStatus.OK)
    public byte[] downloadImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.addHeader("Content-Disposition", "attachment; filename=\"assigned_imgs.zip\"");
        return coachService.downloadImages(id);
    }

    @GetMapping(value = "/count")
    @ResponseStatus(HttpStatus.OK)
    public double getTotalPagesCount() {
        return coachService.getTotalPagesCount();
    }
}
