package spring.app.modules.comments.forum.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.modules.comments.forum.dao.ForumCommentDao;
import spring.app.modules.comments.forum.domain.ForumComment;
import spring.app.modules.comments.forum.dto.ForumCommentCreateDto;
import spring.app.modules.comments.forum.dto.ForumCommentDto;
import spring.app.modules.commons.exception.NotFoundException;
import spring.app.modules.forum.dao.ForumDao;
import spring.app.modules.forum.domain.Forum;
import spring.app.modules.security.dao.UserDao;
import spring.app.modules.security.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ForumCommentServiceImpl implements ForumCommentService, ForumCommentGeneralHandler{
    private final ForumCommentDao forumCommentDao;
    private final UserDao userDao;
    private final ForumDao forumDao;

    public ForumCommentServiceImpl(ForumCommentDao forumCommentDao, UserDao userDao, ForumDao forumDao) {
        this.forumCommentDao = forumCommentDao;
        this.forumDao = forumDao;
        this.userDao = userDao;
    }

    @Override
    public int createForumComment(ForumCommentCreateDto commentCreateDto) {
        validateForumComment(commentCreateDto);
        User user = getUser(commentCreateDto);
        Forum forum = getForum(commentCreateDto);
        ForumComment comment = convertToEntity(commentCreateDto, user, forum, new ForumComment());
        forumCommentDao.save(comment);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateForumComment(Long id, ForumCommentCreateDto commentCreateDto) {
        validateForumComment(commentCreateDto);
        User user = getUser(commentCreateDto);
        Forum forum = getForum(commentCreateDto);
        ForumComment comment = convertToEntity(commentCreateDto, user, forum, new ForumComment());
        forumCommentDao.save(updateContent(comment, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public ForumCommentDto getForumCommentById(Long id) {
        ForumComment byId = getById(id);
        return allInfoDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        forumCommentDao.deleteById(id);
    }

    @Override
    public List<ForumCommentDto> getAllForumComments() {
        List<ForumComment> comments = forumCommentDao.getAllForumComments();
        return listToDto(comments);
    }

    private User getUser(ForumCommentCreateDto commentDto) {
        return userDao.findByEmail(commentDto.getEmailUser()).orElseThrow(() ->
                new NotFoundException("User by email " + commentDto.getEmailUser() + " was not found."));
    }

    private Forum getForum(ForumCommentCreateDto commentDto) {
        return forumDao.getForumById(commentDto.getIdForum()).orElseThrow(() ->
                new NotFoundException("Forum by id " + commentDto.getIdForum() + " was not found."));
    }

    private void validateForumComment(ForumCommentCreateDto forumCommentCreateDto) {
        if (forumCommentCreateDto.getText().isBlank() || Objects.isNull(forumCommentCreateDto.getText())) {
            throw new IllegalArgumentException("ForumComment's text is not valid");
        }
        if (forumCommentCreateDto.getCreatedDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Create date is not valid");
        }
        if (forumCommentCreateDto.getEmailUser().isBlank() || Objects.isNull(forumCommentCreateDto.getEmailUser())) {
            throw new IllegalArgumentException("ForumComment's user email is not valid");
        }
        if (forumCommentCreateDto.getIdForum() == null || Objects.isNull(forumCommentCreateDto.getIdForum())) {
            throw new IllegalArgumentException("ForumComment's idForum is not valid");
        }
    }

    private ForumComment updateContent(ForumComment forumComment, ForumComment resultForumComment) {
        resultForumComment.setText(forumComment.getText());
        resultForumComment.setCreatedDate(forumComment.getCreatedDate());
        return resultForumComment;
    }

    private ForumComment getById(Long id) {
        Optional<ForumComment> resultForumComment = forumCommentDao.getForumCommentById(id);
        if (resultForumComment.isEmpty()) {
            throw new NotFoundException("ForumComment by id was not found!");
        }
        return resultForumComment.get();
    }

    private ForumComment convertToEntity(ForumCommentCreateDto forumCommentCreateDto, User user, Forum forum,  ForumComment forumComment) {
        forumComment.setCreatedDate(forumCommentCreateDto.getCreatedDate());
        forumComment.setText(forumCommentCreateDto.getText());
        forumComment.setCreatedBy(user);
        forumComment.setForum(forum);
        return forumComment;
    }

    @Override
    public List<ForumCommentDto> listToDto(List<ForumComment> forumComments) {
        return ForumCommentGeneralHandler.super.listToDto(forumComments);
    }

    @Override
    public ForumCommentDto allInfoDto(ForumComment forumComment) {
        return ForumCommentGeneralHandler.super.allInfoDto(forumComment);
    }
}
