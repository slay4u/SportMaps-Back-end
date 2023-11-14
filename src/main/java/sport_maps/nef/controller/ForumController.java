package sport_maps.nef.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sport_maps.nef.dto.ForumDto;
import sport_maps.nef.dto.ForumSaveDto;
import sport_maps.nef.service.ForumService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static sport_maps.commons.BaseController.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/forums")
public class ForumController {
    private final ForumService service;

    public ForumController(ForumService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(OK)
    public int createForum(@Valid @RequestBody ForumSaveDto requestToSave) {
        return service.createForum(requestToSave);
    }

    @GetMapping(params = {"page_num"})
    @ResponseStatus(OK)
    public List<ForumDto> getAllForums(int page_num) {
        return service.getAllForums(page_num);
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public ForumDto getForumById(@PathVariable("id") Long id) {
        return service.getForumById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public int updateForum(@PathVariable("id") Long id, @Valid @RequestBody ForumSaveDto requestToSave) {
        return service.updateForum(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void deleteForum(@PathVariable("id") Long id) {
        service.deleteById(id);
    }

    @GetMapping("/count")
    @ResponseStatus(OK)
    public double getTotalPagesCount() {
        return service.getTotalPagesCount();
    }
}
