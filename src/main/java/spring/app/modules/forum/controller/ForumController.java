package spring.app.modules.forum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.app.modules.commons.controller.BaseRestController;
import spring.app.modules.forum.dto.ForumAllInfoDto;
import spring.app.modules.forum.dto.ForumCreateDto;
import spring.app.modules.forum.service.ForumService;

import java.util.List;

import static spring.app.modules.commons.controller.BaseRestController.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "forums")
@AllArgsConstructor
public class ForumController extends BaseRestController {
    private final ForumService forumService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public int createForum(@Valid @RequestBody ForumCreateDto requestToSave) {
        return forumService.createForum(requestToSave);
    }

    @GetMapping(params = {"page_num"})
    @ResponseStatus(HttpStatus.OK)
    public List<ForumAllInfoDto> getAllForums(int page_num) {
        return forumService.getAllForums(page_num);
    }

    @GetMapping("/byId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ForumAllInfoDto getForumById(@PathVariable("id") Long id) {
        return forumService.getForumById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int updateForumById(@Valid @PathVariable("id") Long id,
                             @RequestBody ForumCreateDto requestToSave) {
        return forumService.updateForum(id, requestToSave);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteForumById(@PathVariable("id") Long id) {
        forumService.deleteById(id);
    }

    @GetMapping(value = "/count")
    @ResponseStatus(HttpStatus.OK)
    public double getTotalPagesCount() {
        return forumService.getTotalPagesCount();
    }
}
