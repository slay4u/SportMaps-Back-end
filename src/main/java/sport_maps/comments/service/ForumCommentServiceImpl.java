package sport_maps.comments.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.comments.dao.ForumCommentDao;
import sport_maps.comments.domain.ForumComment;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.commons.service.AbstractService;
import sport_maps.nef.dao.ForumDao;
import sport_maps.nef.domain.Forum;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.User;

@Service("forumCommentServiceImpl")
@Transactional
public class ForumCommentServiceImpl extends AbstractService<ForumComment, ForumCommentDao> implements CommentService {
    private final UserDao userDao;
    private final ForumDao forumDao;
    private final Mapper mapper;

    @Override
    @Autowired
    protected void setDao(ForumCommentDao commentDao) {
        this.dao = commentDao;
    }

    public ForumCommentServiceImpl(UserDao userDao, ForumDao forumDao, Mapper mapper) {
        this.userDao = userDao;
        this.forumDao = forumDao;
        this.mapper = mapper;
    }

    @Override
    public void createComment(CommentSaveDto dto) {
        validateComment(dto);
        save(mapper.convertToEntity(dto, getUser(dto), getForum(dto)));
    }

    @Override
    public void updateComment(Long id, CommentSaveDto dto) {
        validateComment(dto);
        update(mapper.convertToEntity(dto, getUser(dto), getForum(dto)), id);
    }

    @Override
    public void deleteById(Long id) {
        delete(id);
    }

    private User getUser(CommentSaveDto dto) {
        return userDao.findByEmail(dto.author()).orElseThrow(EntityNotFoundException::new);
    }

    private Forum getForum(CommentSaveDto dto) {
        return forumDao.findById(dto.id()).orElseThrow(EntityNotFoundException::new);
    }

    private void validateComment(CommentSaveDto dto) {
        if (dto.text().isBlank()) throw new IllegalArgumentException("Text is not valid.");
        if (dto.author().isBlank()) throw new IllegalArgumentException("User email is not valid.");
    }
}
