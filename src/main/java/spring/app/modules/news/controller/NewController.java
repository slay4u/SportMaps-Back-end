package spring.app.modules.news.controller;

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
import spring.app.modules.news.dto.NewAllInfoDto;
import spring.app.modules.news.dto.NewCreateDto;
import spring.app.modules.news.service.NewService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/sport-maps/v1/news")
@AllArgsConstructor
public class NewController {
    private final NewService newService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public int createNew(@Valid @RequestBody NewCreateDto requestToSave) {
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
    public int updateNewById(@Valid @PathVariable("id") Long id,
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

    @GetMapping(value = "/count")
    @ResponseStatus(HttpStatus.OK)
    public double getTotalPagesCount() {
        return newService.getTotalPagesCount();
    }
}
