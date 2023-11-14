package sport_maps.comments.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.comments.dto.CommentDto;
import sport_maps.comments.service.CommentService;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static sport_maps.commons.BaseController.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/event-comments")
public class EventCommentController {
    @Qualifier("eventCommentServiceImpl")
    private final CommentService service;

    public EventCommentController(@Qualifier("eventCommentServiceImpl") CommentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(OK)
    public int createComment(@Valid @RequestBody CommentSaveDto requestToSave) {
        return service.createComment(requestToSave);
    }

    @GetMapping
    @ResponseStatus(OK)
    public List<CommentDto> getAllComments() {
        return service.getAllComments();
    }

    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public CommentDto getCommentById(@PathVariable("id") Long id) {
        return service.getCommentById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(OK)
    public int updateComment(@PathVariable("id") Long id, @Valid @RequestBody CommentSaveDto requestToSave) {
        return service.updateComment(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(OK)
    public void deleteComment(@PathVariable("id") Long id) {
        service.deleteById(id);
    }
}
