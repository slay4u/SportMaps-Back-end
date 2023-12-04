package sport_maps.comments.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.comments.dao.ForumCommentDao;
import sport_maps.comments.domain.ForumComment;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.nef.dao.ForumDao;
import sport_maps.nef.domain.Forum;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.User;

import java.time.LocalDateTime;

@Service("forumCommentServiceImpl")
@Transactional
public class ForumCommentServiceImpl implements CommentService {
    private final ForumCommentDao commentDao;
    private final UserDao userDao;
    private final ForumDao forumDao;
    private final Mapper mapper;

    public ForumCommentServiceImpl(ForumCommentDao commentDao, UserDao userDao, ForumDao forumDao, Mapper mapper) {
        this.commentDao = commentDao;
        this.userDao = userDao;
        this.forumDao = forumDao;
        this.mapper = mapper;
    }

    @Override
    public void createComment(CommentSaveDto dto) {
        validateComment(dto);
        User user = getUser(dto);
        Forum forum = getForum(dto);
        ForumComment comment = mapper.convertToEntity(dto, user, forum, new ForumComment());
        commentDao.save(comment);
    }

    @Override
    public void updateComment(Long id, CommentSaveDto dto) {
        validateComment(dto);
        User user = getUser(dto);
        Forum forum = getForum(dto);
        ForumComment comment = mapper.convertToEntity(dto, user, forum, new ForumComment());
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

    private Forum getForum(CommentSaveDto dto) {
        return forumDao.findById(dto.id()).orElseThrow(() -> new EntityNotFoundException("Forum wasn't found."));
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

    private ForumComment updateContent(ForumComment comment, ForumComment resultComment) {
        resultComment.setText(comment.getText());
        resultComment.setDate(comment.getDate());
        return resultComment;
    }

    private ForumComment getById(Long id) {
        return commentDao.findById(id).orElseThrow(() -> new EntityNotFoundException("ForumComment wasn't found."));
    }
}
