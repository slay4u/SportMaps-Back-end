package sport_maps.comments.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.comments.dao.ForumCommentDao;
import sport_maps.comments.domain.ForumComment;
import sport_maps.comments.dto.CommentDto;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.nef.dao.ForumDao;
import sport_maps.nef.domain.Forum;
import sport_maps.commons.util.mapper.CustomObjectMapper;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service("forumCommentServiceImpl")
@Transactional
public class ForumCommentServiceImpl implements CommentService {
    private final ForumCommentDao forumCommentDao;
    private final UserDao userDao;
    private final ForumDao forumDao;
    private final CustomObjectMapper mapper;

    public ForumCommentServiceImpl(ForumCommentDao forumCommentDao, UserDao userDao, ForumDao forumDao,
                                   CustomObjectMapper mapper) {
        this.forumCommentDao = forumCommentDao;
        this.userDao = userDao;
        this.forumDao = forumDao;
        this.mapper = mapper;
    }

    @Override
    public int createComment(CommentSaveDto dto) {
        validateForumComment(dto);
        User user = getUser(dto);
        Forum forum = getForum(dto);
        ForumComment comment = mapper.convertToEntity(dto, user, forum, new ForumComment());
        forumCommentDao.save(comment);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateComment(Long id, CommentSaveDto dto) {
        validateForumComment(dto);
        User user = getUser(dto);
        Forum forum = getForum(dto);
        ForumComment comment = mapper.convertToEntity(dto, user, forum, new ForumComment());
        forumCommentDao.save(updateContent(comment, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public CommentDto getCommentById(Long id) {
        ForumComment byId = getById(id);
        return mapper.toCommentDto(byId);
    }

    @Override
    public List<CommentDto> getAllComments() {
        List<ForumComment> comments = forumCommentDao.findAll();
        return mapper.toListCommentDto(comments);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        forumCommentDao.deleteById(id);
    }

    private User getUser(CommentSaveDto dto) {
        return userDao.findByEmail(dto.createdBy()).orElseThrow(() ->
                new EntityNotFoundException("User by email " + dto.createdBy() + " was not found."));
    }

    private Forum getForum(CommentSaveDto dto) {
        return forumDao.findById(dto.id()).orElseThrow(() ->
                new EntityNotFoundException("Forum by id " + dto.id() + " was not found."));
    }

    private void validateForumComment(CommentSaveDto dto) {
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

    private ForumComment updateContent(ForumComment forumComment, ForumComment resultForumComment) {
        resultForumComment.setText(forumComment.getText());
        resultForumComment.setDate(forumComment.getDate());
        return resultForumComment;
    }

    private ForumComment getById(Long id) {
        Optional<ForumComment> resultForumComment = forumCommentDao.findById(id);
        if (resultForumComment.isEmpty()) {
            throw new EntityNotFoundException("ForumComment by id was not found!");
        }
        return resultForumComment.get();
    }
}
