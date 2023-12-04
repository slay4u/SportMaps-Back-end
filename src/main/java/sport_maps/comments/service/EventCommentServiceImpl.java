package sport_maps.comments.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.comments.dao.EventCommentDao;
import sport_maps.comments.domain.EventComment;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.nef.dao.EventDao;
import sport_maps.nef.domain.Event;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.User;

import java.time.LocalDateTime;

@Service("eventCommentServiceImpl")
@Transactional
public class EventCommentServiceImpl implements CommentService {
    private final EventCommentDao commentDao;
    private final UserDao userDao;
    private final EventDao eventDao;
    private final Mapper mapper;

    public EventCommentServiceImpl(EventCommentDao commentDao, UserDao userDao, EventDao eventDao, Mapper mapper) {
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.eventDao = eventDao;
        this.mapper = mapper;
    }

    @Override
    public void createComment(CommentSaveDto dto) {
        validateComment(dto);
        User user = getUser(dto);
        Event event = getEvent(dto);
        EventComment comment = mapper.convertToEntity(dto, user, event, new EventComment());
        commentDao.save(comment);
    }

    @Override
    public void updateComment(Long id, CommentSaveDto dto) {
        validateComment(dto);
        User user = getUser(dto);
        Event event = getEvent(dto);
        EventComment comment = mapper.convertToEntity(dto, user, event, new EventComment());
        commentDao.save(updateContent(comment, getById(id)));
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        commentDao.deleteById(id);
    }

    private User getUser(CommentSaveDto dto) {
        return userDao.findByEmail(dto.author()).orElseThrow(() -> new EntityNotFoundException("User wasn't found."));
    }

    private Event getEvent(CommentSaveDto dto) {
        return eventDao.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("Event wasn't found."));
    }

    private void validateComment(CommentSaveDto dto) {
        if (dto.text().isBlank()) {
            throw new IllegalArgumentException("Text is not valid.");
        }
        if (dto.date().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date is not valid.");
        }
        if (dto.author().isBlank()) {
            throw new IllegalArgumentException("User email is not valid.");
        }
    }

    private EventComment updateContent(EventComment comment, EventComment resultComment) {
        resultComment.setText(comment.getText());
        resultComment.setDate(comment.getDate());
        return resultComment;
    }

    private EventComment getById(Long id) {
        return commentDao.findById(id).orElseThrow(() -> new EntityNotFoundException("EventComment wasn't found."));
    }
}
