package spring.app.modules.comments.eventComment.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.modules.comments.eventComment.dao.EventCommentDao;
import spring.app.modules.comments.eventComment.domain.EventComment;
import spring.app.modules.comments.eventComment.dto.EventCommentCreateDto;
import spring.app.modules.comments.eventComment.dto.EventCommentDto;
import spring.app.modules.commons.exception.NotFoundException;
import spring.app.modules.event.dao.EventDao;
import spring.app.modules.event.domain.Event;
import spring.app.modules.security.dao.UserDao;
import spring.app.modules.security.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class EventCommentServiceImpl implements EventCommentService, EventCommentGeneralHandler {
    private final EventCommentDao eventCommentDao;
    private final UserDao userDao;
    private final EventDao eventDao;

    public EventCommentServiceImpl(EventCommentDao eventCommentDao, UserDao userDao, EventDao eventDao) {
        this.eventCommentDao = eventCommentDao;
        this.userDao = userDao;
        this.eventDao = eventDao;
    }

    @Override
    public int createEventComment(EventCommentCreateDto eventCommentCreateDto) {
        validateEventComment(eventCommentCreateDto);
        User user = getUser(eventCommentCreateDto);
        Event event = getEvent(eventCommentCreateDto);
        EventComment comment = convertToEntity(eventCommentCreateDto, user, event, new EventComment());
        eventCommentDao.save(comment);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateEventComment(Long id, EventCommentCreateDto commentDto) {
        validateEventComment(commentDto);
        User user = getUser(commentDto);
        Event event = getEvent(commentDto);
        EventComment comment = convertToEntity(commentDto, user, event, new EventComment());
        eventCommentDao.save(updateContent(comment, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public EventCommentDto getEventCommentById(Long id) {
        EventComment byId = getById(id);
        return allInfoDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        eventCommentDao.deleteById(id);
    }

    @Override
    public List<EventCommentDto> getAllEventComments() {
        List<EventComment> comments = eventCommentDao.getAllEventComments();
        return listToDto(comments);
    }

    private User getUser(EventCommentCreateDto commentDto) {
        return userDao.findByEmail(commentDto.getEmailUser()).orElseThrow(() ->
                new NotFoundException("User by email " + commentDto.getEmailUser() + " was not found."));
    }

    private Event getEvent(EventCommentCreateDto commentDto) {
        return eventDao.getEventById(commentDto.getIdEvent()).orElseThrow(() ->
                new NotFoundException("Event by id " + commentDto.getIdEvent() + " was not found."));
    }

    private void validateEventComment(EventCommentCreateDto eventCommentCreateDto) {
        if (eventCommentCreateDto.getText().isBlank() || Objects.isNull(eventCommentCreateDto.getText())) {
            throw new IllegalArgumentException("EventComment's text is not valid");
        }
        if (eventCommentCreateDto.getCreatedDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Create date is not valid");
        }
        if (eventCommentCreateDto.getEmailUser().isBlank() || Objects.isNull(eventCommentCreateDto.getEmailUser())) {
            throw new IllegalArgumentException("EventComment's user email is not valid");
        }
        if (eventCommentCreateDto.getIdEvent() == null || Objects.isNull(eventCommentCreateDto.getIdEvent())) {
            throw new IllegalArgumentException("EventComment's idEvent is not valid");
        }
    }

    private EventComment updateContent(EventComment eventComment, EventComment resultEventComment) {
        resultEventComment.setText(eventComment.getText());
        resultEventComment.setCreatedDate(eventComment.getCreatedDate());
        return resultEventComment;
    }

    private EventComment getById(Long id) {
        Optional<EventComment> resultEventComment = eventCommentDao.getEventCommentById(id);
        if (resultEventComment.isEmpty()) {
            throw new NotFoundException("EventComment by id was not found!");
        }
        return resultEventComment.get();
    }

    private EventComment convertToEntity(EventCommentCreateDto eventCommentCreateDto, User user, Event event, EventComment eventComment) {
        eventComment.setCreatedDate(eventCommentCreateDto.getCreatedDate());
        eventComment.setText(eventCommentCreateDto.getText());
        eventComment.setCreatedBy(user);
        eventComment.setEvent(event);
        return eventComment;
    }

    @Override
    public List<EventCommentDto> listToDto(List<EventComment> eventCommentList) {
        return EventCommentGeneralHandler.super.listToDto(eventCommentList);
    }

    @Override
    public EventCommentDto allInfoDto(EventComment eventComment) {
        return EventCommentGeneralHandler.super.allInfoDto(eventComment);
    }
}
