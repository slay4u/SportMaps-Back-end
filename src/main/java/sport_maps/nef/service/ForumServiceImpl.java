package sport_maps.nef.service;

import jakarta.persistence.EntityNotFoundException;
import sport_maps.commons.util.mapper.CustomObjectMapper;
import sport_maps.nef.dao.ForumDao;
import sport_maps.nef.dto.ForumDto;
import sport_maps.nef.dto.ForumSaveDto;
import sport_maps.security.dao.UserDao;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.nef.domain.Forum;
import sport_maps.security.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ForumServiceImpl implements ForumService {
    private final int PAGE_ELEMENTS_AMOUNT = 15;
    private final ForumDao forumDao;
    private final UserDao userDao;
    private final CustomObjectMapper mapper;

    public ForumServiceImpl(ForumDao forumDao, UserDao userDao, CustomObjectMapper mapper) {
        this.forumDao = forumDao;
        this.userDao = userDao;
        this.mapper = mapper;
    }

    @Override
    public int createForum(ForumSaveDto dto) {
        validateForum(dto);
        validateForumName(dto.name());
        User user = getUser(dto);
        Forum forum = mapper.convertToEntity(dto, user,new Forum());
        forumDao.save(forum);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateForum(Long id, ForumSaveDto dto) {
        validateForum(dto);
        User user = getUser(dto);
        Forum forum = mapper.convertToEntity(dto, user, new Forum());
        forumDao.save(updateContent(forum, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public ForumDto getForumById(Long id) {
        Forum byId = getById(id);
        return mapper.toForumDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        forumDao.deleteById(id);
    }

    @Override
    public List<ForumDto> getAllForums(int pageNumber) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be less than 0!");
        }
        List<Forum> forums = forumDao.findAll(PageRequest.of(pageNumber, PAGE_ELEMENTS_AMOUNT)).getContent();
        return mapper.toListForumDto(forums);
    }

    @Override
    public double getTotalPagesCount() {
        long count = forumDao.getAllForumCount();
        double pagesNum = (double) count / PAGE_ELEMENTS_AMOUNT;
        return Math.ceil(pagesNum);
    }

    private User getUser(ForumSaveDto dto) {
        return userDao.findByEmail(dto.createdBy()).orElseThrow(() ->
                new EntityNotFoundException("User by email " + dto.createdBy() + " was not found."));
    }

    private void validateForum(ForumSaveDto dto) {
        if (dto.name().isBlank()) {
            throw new IllegalArgumentException("Forum's name is not valid");
        }
        if (dto.date().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Create date is not valid");
        }
        if (dto.text().isBlank()) {
            throw new IllegalArgumentException("Description is not valid");
        }
        if (dto.createdBy().isBlank()) {
            throw new IllegalArgumentException("User email is not valid");
        }
    }

    private void validateForumName(String name) {
        Optional<Forum> byName = forumDao.findForumByName(name);
        if (byName.isPresent()) {
            throw new IllegalArgumentException("Forum with the name "
                    + name +
                    " already exists!");
        }
    }

    private Forum updateContent(Forum forum, Forum resultForum) {
        resultForum.setName(forum.getName());
        resultForum.setDate(forum.getDate());
        resultForum.setText(forum.getText());
        return resultForum;
    }

    private Forum getById(Long id) {
        Optional<Forum> resultForum = forumDao.findById(id);
        if (resultForum.isEmpty()) {
            throw new EntityNotFoundException("Forum by id was not found!");
        }
        return resultForum.get();
    }
}
