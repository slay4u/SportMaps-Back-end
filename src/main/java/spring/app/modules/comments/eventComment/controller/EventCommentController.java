package spring.app.modules.comments.eventComment.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import spring.app.modules.comments.eventComment.dto.EventCommentCreateDto;
import spring.app.modules.comments.eventComment.dto.EventCommentDto;
import spring.app.modules.comments.eventComment.service.EventCommentService;

import java.util.List;

@RestController
@RequestMapping("/sport-maps/v1/event-comments")
@AllArgsConstructor
public class EventCommentController {
    private final EventCommentService commentService;

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public int createComment(@RequestBody EventCommentCreateDto eventCreateDto) {
        return commentService.createEventComment(eventCreateDto);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<EventCommentDto> getAllEventComments() {
        return commentService.getAllEventComments();
    }

    @GetMapping("/byId/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EventCommentDto getEventCommentById(@PathVariable("id") Long id) {
        return commentService.getEventCommentById(id);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public int updateEventCommentById(@Valid @PathVariable("id") Long id, EventCommentCreateDto requestToSave) {
        return commentService.updateEventComment(id, requestToSave);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteEventCommentById(@PathVariable("id") Long id) {
        commentService.deleteById(id);
    }
}