package sport_maps.nef.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.nef.dao.ForumDao;
import sport_maps.nef.dto.ForumDto;
import sport_maps.nef.dto.ForumSaveDto;
import sport_maps.security.dao.UserDao;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.nef.domain.Forum;
import sport_maps.security.domain.User;

import java.time.LocalDateTime;

@Service
@Transactional
public class ForumServiceImpl implements ForumService {
    private final ForumDao forumDao;
    private final UserDao userDao;
    private final Mapper mapper;

    public ForumServiceImpl(ForumDao forumDao, UserDao userDao, Mapper mapper) {
        this.forumDao = forumDao;
        this.userDao = userDao;
        this.mapper = mapper;
    }

    @Override
    public void createForum(ForumSaveDto dto) {
        validateForum(dto);
        User user = getUser(dto);
        Forum forum = mapper.convertToEntity(dto, user,new Forum());
        forumDao.save(forum);
    }

    @Override
    public void updateForum(Long id, ForumSaveDto dto) {
        validateForum(dto);
        User user = getUser(dto);
        Forum forum = mapper.convertToEntity(dto, user, new Forum());
        forumDao.save(updateContent(forum, getById(id)));
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
    public Page<ForumDto> getAllForums(int page) {
        return forumDao.findAll(PageRequest.of(page, size)).map(mapper::toForumDto);
    }

    private User getUser(ForumSaveDto dto) {
        return userDao.findByEmail(dto.author()).orElseThrow(() -> new EntityNotFoundException("User wasn't found."));
    }

    private void validateForum(ForumSaveDto dto) {
        if (dto.name().isBlank()) {
            throw new IllegalArgumentException("Name is not valid.");
        }
        if (dto.date().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date is not valid.");
        }
        if (dto.text().isBlank()) {
            throw new IllegalArgumentException("Text is not valid.");
        }
        if (dto.author().isBlank()) {
            throw new IllegalArgumentException("User email is not valid.");
        }
    }

    private Forum updateContent(Forum forum, Forum resultForum) {
        resultForum.setName(forum.getName());
        resultForum.setDate(forum.getDate());
        resultForum.setText(forum.getText());
        return resultForum;
    }

    private Forum getById(Long id) {
        return forumDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Forum wasn't found."));
    }
}
