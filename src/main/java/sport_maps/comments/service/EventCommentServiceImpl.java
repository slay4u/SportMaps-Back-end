package sport_maps.comments.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.comments.dao.EventCommentDao;
import sport_maps.comments.domain.EventComment;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.commons.service.AbstractService;
import sport_maps.nef.dao.EventDao;
import sport_maps.nef.domain.Event;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.User;

@Service("eventCommentServiceImpl")
@Transactional
public class EventCommentServiceImpl extends AbstractService<EventComment, EventCommentDao> implements CommentService {
    private final UserDao userDao;
    private final EventDao eventDao;
    private final Mapper mapper;

    @Override
    @Autowired
    protected void setDao(EventCommentDao commentDao) {
        this.dao = commentDao;
    }

    public EventCommentServiceImpl(UserDao userDao, EventDao eventDao, Mapper mapper) {
        this.userDao = userDao;
        this.eventDao = eventDao;
        this.mapper = mapper;
    }

    @Override
    public void createComment(CommentSaveDto dto) {
        validateComment(dto);
        save(mapper.convertToEntity(dto, getUser(dto), getEvent(dto)));
    }

    @Override
    public void updateComment(Long id, CommentSaveDto dto) {
        validateComment(dto);
        update(mapper.convertToEntity(dto, getUser(dto), getEvent(dto)), id);
    }

    @Override
    public void deleteById(Long id) {
        delete(id);
    }

    private User getUser(CommentSaveDto dto) {
        return userDao.findByEmail(dto.author()).orElseThrow(EntityNotFoundException::new);
    }

    private Event getEvent(CommentSaveDto dto) {
        return eventDao.findById(dto.id()).orElseThrow(EntityNotFoundException::new);
    }

    private void validateComment(CommentSaveDto dto) {
        if (dto.text().isBlank()) throw new IllegalArgumentException("Text is not valid.");
        if (dto.author().isBlank()) throw new IllegalArgumentException("User email is not valid.");
    }
}
