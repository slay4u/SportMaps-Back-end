package sport_maps.nef.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import sport_maps.nef.dto.NewsDto;
import sport_maps.nef.dto.NewsSaveDto;
import sport_maps.nef.service.NewsService;
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

import static sport_maps.commons.BaseController.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/news")
public class NewsController {
    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNews(@Valid @RequestBody NewsSaveDto requestToSave) {
        service.createNews(requestToSave);
    }

    @GetMapping(params = {"page"})
    @ResponseStatus(HttpStatus.OK)
    public Page<NewsDto> getAllNews(@RequestParam("page") int page) {
        return service.getAllNews(page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public NewsDto getNewsById(@PathVariable("id") Long id) {
        return service.getNewsById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateNews(@PathVariable("id") Long id, @Valid @RequestBody NewsSaveDto requestToSave) {
        service.updateNews(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNews(@PathVariable("id") Long id) {
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
