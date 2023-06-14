package spring.app.modules.forum.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.app.modules.comments.forum.dao.ForumCommentDao;
import spring.app.modules.comments.forum.domain.ForumComment;
import spring.app.modules.commons.exception.NotFoundException;
import spring.app.modules.forum.dao.ForumDao;
import spring.app.modules.forum.domain.Forum;
import spring.app.modules.forum.dto.ForumAllInfoDto;
import spring.app.modules.forum.dto.ForumCreateDto;
import spring.app.modules.security.dao.UserDao;
import spring.app.modules.security.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class ForumServiceImpl implements ForumService, ForumGeneralHandler {
    private final int PAGE_ELEMENTS_AMOUNT = 15;
    private final ForumDao forumDao;
    private final ForumCommentDao forumCommentDao;
    private final UserDao userDao;

    public ForumServiceImpl(ForumDao forumDao, ForumCommentDao forumCommentDao, UserDao userDao) {
        this.forumDao = forumDao;
        this.forumCommentDao = forumCommentDao;
        this.userDao = userDao;
    }

    @Override
    public int createForum(ForumCreateDto forumCreateDto) {
        validateForum(forumCreateDto);
        validateForumName(forumCreateDto.getName());
        User user = getUser(forumCreateDto);
        Forum forum = convertToEntity(forumCreateDto, user,new Forum());
        forumDao.save(forum);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateForum(Long id, ForumCreateDto forumCreateDto) {
        validateForum(forumCreateDto);
        User user = getUser(forumCreateDto);
        Forum forum = convertToEntity(forumCreateDto, user, new Forum());
        forumDao.save(updateContent(forum, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public ForumAllInfoDto getForumById(Long id) {
        Forum byId = getById(id);
        return allInfoDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        forumDao.deleteById(id);
    }

    @Override
    public List<ForumAllInfoDto> getAllForums(int pageNumber) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be less than 0!");
        }
        List<Forum> forums = forumDao.getAllForums(PageRequest.of(pageNumber, PAGE_ELEMENTS_AMOUNT));
        return listToDto(forums);
    }

    @Override
    public double getTotalPagesCount() {
        long count = forumDao.getAllForumCount();
        double pagesNum = (double) count / PAGE_ELEMENTS_AMOUNT;
        return Math.ceil(pagesNum);
    }

    private User getUser(ForumCreateDto forumCreateDto) {
        return userDao.findByEmail(forumCreateDto.getEmailUser()).orElseThrow(() ->
                new NotFoundException("User by email " + forumCreateDto.getEmailUser() + " was not found."));
    }

    private void validateForum(ForumCreateDto forumCreateDto) {
        if (forumCreateDto.getName().isBlank() || Objects.isNull(forumCreateDto.getName())) {
            throw new IllegalArgumentException("Forum's name is not valid");
        }
        if (forumCreateDto.getCreateDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Create date is not valid");
        }
        if (forumCreateDto.getDesc().isBlank() || Objects.isNull(forumCreateDto.getDesc())) {
            throw new IllegalArgumentException("Description is not valid");
        }
        if (forumCreateDto.getEmailUser().isBlank() || Objects.isNull(forumCreateDto.getEmailUser())) {
            throw new IllegalArgumentException("User email is not valid");
        }
    }

    private void validateForumName(String name) {
        Optional<Forum> byName = forumDao.getForumByName(name);
        if (byName.isPresent()) {
            throw new IllegalArgumentException("Forum with the name "
                    + name +
                    " already exists!");
        }
    }

    private Forum updateContent(Forum forum, Forum resultForum) {
        resultForum.setName(forum.getName());
        resultForum.setCreateDate(forum.getCreateDate());
        resultForum.setDescription(forum.getDescription());
        return resultForum;
    }

    private Forum getById(Long id) {
        Optional<Forum> resultForum = forumDao.getForumById(id);
        if (resultForum.isEmpty()) {
            throw new NotFoundException("Forum by id was not found!");
        }
        return resultForum.get();
    }

    private Forum convertToEntity(ForumCreateDto forumCreateDto, User user, Forum forum) {
        forum.setName(forumCreateDto.getName());
        forum.setCreateDate(forumCreateDto.getCreateDate());
        forum.setDescription(forumCreateDto.getDesc());
        forum.setCreatedBy(user);
        return forum;
    }

    private List<ForumComment> fetchForumComments(Long id) {
        List<ForumComment> comments = forumCommentDao.findAllByForumId(id);
        if (comments.isEmpty()) {
            return null;
        }
        return comments;
    }

    @Override
    public List<ForumAllInfoDto> listToDto(List<Forum> forums) {
        return ForumGeneralHandler.super.listToDto(forums);
    }

    @Override
    public ForumAllInfoDto allInfoDto(Forum forum) {
        ForumAllInfoDto forumAllInfoDto = ForumGeneralHandler.super.allInfoDto(forum);
        forumAllInfoDto.setCommentList(fetchForumComments(forum.getIdForum()));
        return forumAllInfoDto;
    }
}
