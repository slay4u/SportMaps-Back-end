package sport_maps.nef.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.image.service.ImageService;
import sport_maps.nef.dto.NewsDto;
import sport_maps.nef.dto.NewsSaveDto;
import sport_maps.nef.service.NewsService;

import java.io.IOException;

import static sport_maps.security.general.SecurityURLs.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/news")
public class NewsController {
    private final NewsService service;
    @Qualifier("newsImageServiceImpl")
    private final ImageService imageService;

    public NewsController(NewsService service, @Qualifier("newsImageServiceImpl") ImageService imageService) {
        this.service = service;
        this.imageService = imageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createNews(@Valid @RequestPart("news") NewsSaveDto requestToSave, @RequestPart MultipartFile image) throws IOException {
        service.createNews(requestToSave);
        imageService.uploadImage(requestToSave.name(), image);
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
    public void updateNews(@PathVariable("id") Long id, @Valid @RequestPart("news") NewsSaveDto requestToSave,
                           @RequestPart(required = false) MultipartFile image) throws IOException {
        if (image != null) imageService.updateImage(id, image);
        service.updateNews(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNews(@PathVariable("id") Long id) throws IOException {
        imageService.deleteAllImages(id);
        service.deleteById(id);
    }
}
