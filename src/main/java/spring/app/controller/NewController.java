package spring.app.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.app.dto.news.NewAllInfoDto;
import spring.app.dto.news.NewCreateDto;
import spring.app.dto.news.NewInfoDto;
import spring.app.service.NewService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/sport-maps/v1/news")
@AllArgsConstructor
public class NewController {
    private final NewService newService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public NewInfoDto createNew(@Valid @RequestBody NewCreateDto requestToSave) {
        return newService.createNew(requestToSave);
    }

    @GetMapping(params = {"page_num"})
    @ResponseStatus(HttpStatus.OK)
    public List<NewAllInfoDto> getAllNews(int page_num) {
        return newService.getAllNews(page_num);
    }

    @GetMapping("/byId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewAllInfoDto getNewById(@PathVariable("id") Long id) {
        return newService.getNewById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewInfoDto updateNewById(@Valid @PathVariable("id") Long id,
                                      @RequestBody NewCreateDto requestToSave) {
        return newService.updateNew(id, requestToSave);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteNewById(@PathVariable("id") Long id) {
        newService.deleteById(id);
    }

    @PostMapping("/photo/upload/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> uploadImage(@PathVariable("id") Long id,
                                         @RequestParam ("image") MultipartFile file) throws IOException {
        String uploadImage = newService.uploadImage(file, id);
        return ResponseEntity.ok(uploadImage);
    }

    @GetMapping(value = "/photo/download/{id}", produces="application/zip")
    @ResponseStatus(HttpStatus.OK)
    public byte[] downloadImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.addHeader("Content-Disposition", "attachment; filename=\"assigned_imgs.zip\"");
        return newService.downloadImages(id);
    }

    @GetMapping(value = "/count")
    @ResponseStatus(HttpStatus.OK)
    public double getTotalPagesCount() {
        return newService.getTotalPagesCount();
    }
}
