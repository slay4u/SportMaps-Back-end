package sport_maps.comments.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.comments.dao.EventCommentDao;
import sport_maps.comments.domain.EventComment;
import sport_maps.comments.dto.CommentDto;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.nef.dao.EventDao;
import sport_maps.nef.domain.Event;
import sport_maps.commons.util.mapper.CustomObjectMapper;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("eventCommentServiceImpl")
@Transactional
public class EventCommentServiceImpl implements CommentService {
    private final EventCommentDao eventCommentDao;
    private final UserDao userDao;
    private final EventDao eventDao;
    private final CustomObjectMapper mapper;

    public EventCommentServiceImpl(EventCommentDao eventCommentDao, UserDao userDao, EventDao eventDao,
                                   CustomObjectMapper mapper) {
        this.eventCommentDao = eventCommentDao;
        this.userDao = userDao;
        this.eventDao = eventDao;
        this.mapper = mapper;
    }

    @Override
    public int createComment(CommentSaveDto dto) {
        validateEventComment(dto);
        User user = getUser(dto);
        Event event = getEvent(dto);
        EventComment comment = mapper.convertToEntity(dto, user, event, new EventComment());
        eventCommentDao.save(comment);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateComment(Long id, CommentSaveDto dto) {
        validateEventComment(dto);
        User user = getUser(dto);
        Event event = getEvent(dto);
        EventComment comment = mapper.convertToEntity(dto, user, event, new EventComment());
        eventCommentDao.save(updateContent(comment, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public CommentDto getCommentById(Long id) {
        EventComment byId = getById(id);
        return mapper.toCommentDto(byId);
    }

    @Override
    public List<CommentDto> getAllComments() {
        List<EventComment> comments = eventCommentDao.findAll();
        return mapper.toListCommentDto(comments);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        eventCommentDao.deleteById(id);
    }

    private User getUser(CommentSaveDto dto) {
        return userDao.findByEmail(dto.createdBy()).orElseThrow(() ->
                new EntityNotFoundException("User by email " + dto.createdBy() + " was not found."));
    }

    private Event getEvent(CommentSaveDto dto) {
        return eventDao.findById(dto.id()).orElseThrow(() ->
                new EntityNotFoundException("Event by id " + dto.id() + " was not found."));
    }

    private void validateEventComment(CommentSaveDto dto) {
        if (dto.text().isBlank()) {
            throw new IllegalArgumentException("Text is not valid");
        }
        if (dto.date().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date is not valid");
        }
        if (dto.createdBy().isBlank()) {
            throw new IllegalArgumentException("User email is not valid");
        }
    }

    private EventComment updateContent(EventComment eventComment, EventComment resultEventComment) {
        resultEventComment.setText(eventComment.getText());
        resultEventComment.setDate(eventComment.getDate());
        return resultEventComment;
    }

    private EventComment getById(Long id) {
        Optional<EventComment> resultEventComment = eventCommentDao.findById(id);
        if (resultEventComment.isEmpty()) {
            throw new EntityNotFoundException("EventComment by id was not found!");
        }
        return resultEventComment.get();
    }
}
