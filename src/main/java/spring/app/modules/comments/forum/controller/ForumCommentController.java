package spring.app.modules.comments.forum.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.app.modules.comments.forum.dto.ForumCommentCreateDto;
import spring.app.modules.comments.forum.dto.ForumCommentDto;
import spring.app.modules.comments.forum.service.ForumCommentService;

import java.util.List;

@RestController
@RequestMapping("/sport-maps/v1/forum-comments")
@AllArgsConstructor
public class ForumCommentController {
    private final ForumCommentService forumCommentService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public int createComment(@Valid @RequestBody ForumCommentCreateDto requestToSave) {
        return forumCommentService.createForumComment(requestToSave);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<ForumCommentDto> getAllForumComments() {
        return forumCommentService.getAllForumComments();
    }

    @GetMapping("/byId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ForumCommentDto getForumCommentById(@PathVariable("id") Long id) {
        return forumCommentService.getForumCommentById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int updateForumCommentById(@Valid @PathVariable("id") Long id, @RequestBody ForumCommentCreateDto requestToSave) {
        return forumCommentService.updateForumComment(id, requestToSave);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteForumCommentById(@PathVariable("id") Long id) {
        forumCommentService.deleteById(id);
    }
}
