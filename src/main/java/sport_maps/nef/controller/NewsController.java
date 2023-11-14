package sport_maps.nef.controller;

import jakarta.validation.Valid;
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
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static sport_maps.commons.BaseController.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/news")
public class NewsController {
    private final NewsService service;

    public NewsController(NewsService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(OK)
    public int createNews(@Valid @RequestBody NewsSaveDto requestToSave) {
        return service.createNew(requestToSave);
    }

    @GetMapping(params = {"page_num"})
    @ResponseStatus(OK)
    public List<NewsDto> getAllNews(int page_num) {
        return service.getAllNews(page_num);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public NewsDto getNewsById(@PathVariable("id") Long id) {
        return service.getNewById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public int updateNews(@PathVariable("id") Long id, @Valid @RequestBody NewsSaveDto requestToSave) {
        return service.updateNew(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void deleteNews(@PathVariable("id") Long id) {
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
