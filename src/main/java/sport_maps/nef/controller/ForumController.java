package sport_maps.nef.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sport_maps.nef.dto.ForumDto;
import sport_maps.nef.dto.ForumSaveDto;
import sport_maps.nef.service.ForumService;

import static sport_maps.security.general.SecurityURLs.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/forums")
public class ForumController {
    private final ForumService service;

    public ForumController(ForumService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createForum(@Valid @RequestBody ForumSaveDto requestToSave) {
        service.createForum(requestToSave);
    }

    @GetMapping(params = {"page"})
    @ResponseStatus(HttpStatus.OK)
    public Page<ForumDto> getAllForums(@RequestParam("page") int page) {
        return service.getAllForums(page);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ForumDto getForumById(@PathVariable("id") Long id) {
        return service.getForumById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateForum(@PathVariable("id") Long id, @Valid @RequestBody ForumSaveDto requestToSave) {
        service.updateForum(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteForum(@PathVariable("id") Long id) {
        service.deleteById(id);
    }
}
