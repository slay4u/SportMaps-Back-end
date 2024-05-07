package sport_maps.comments.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.comments.service.CommentService;

import static sport_maps.security.general.SecurityURLs.BASE_URL;

@RestController
@RequestMapping(BASE_URL + "/event-comments")
public class EventCommentController {
    @Qualifier("eventCommentServiceImpl")
    private final CommentService service;

    public EventCommentController(@Qualifier("eventCommentServiceImpl") CommentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(@Valid @RequestBody CommentSaveDto requestToSave) {
        service.createComment(requestToSave);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateComment(@PathVariable("id") Long id, @Valid @RequestBody CommentSaveDto requestToSave) {
        service.updateComment(id, requestToSave);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("id") Long id) {
        service.deleteById(id);
    }
}
