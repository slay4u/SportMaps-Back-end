package sport_maps.nef.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import sport_maps.commons.service.AbstractService;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.nef.dao.ForumDao;
import sport_maps.nef.dto.ForumDto;
import sport_maps.nef.dto.ForumSaveDto;
import sport_maps.security.dao.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.nef.domain.Forum;
import sport_maps.security.domain.User;

@Service
@Transactional
public class ForumServiceImpl extends AbstractService<Forum, ForumDao> implements ForumService {
    private final UserDao userDao;
    private final Mapper mapper;

    @Override
    @Autowired
    protected void setDao(ForumDao forumDao) {
        this.dao = forumDao;
    }

    public ForumServiceImpl(UserDao userDao, Mapper mapper) {
        this.userDao = userDao;
        this.mapper = mapper;
    }

    @Override
    public void createForum(ForumSaveDto dto) {
        validateForum(dto);
        save(mapper.convertToEntity(dto, getUser(dto)));
    }

    @Override
    public void updateForum(Long id, ForumSaveDto dto) {
        validateForum(dto);
        update(mapper.convertToEntity(dto, getUser(dto)), id);
    }

    @Override
    public ForumDto getForumById(Long id) {
        return mapper.convertToDto(getById(id));
    }

    @Override
    public void deleteById(Long id) {
        delete(id);
    }

    @Override
    public Page<ForumDto> getAllForums(int page) {
        return getAll(page, size).map(mapper::convertToDto);
    }

    private User getUser(ForumSaveDto dto) {
        return userDao.findByEmail(dto.author()).orElseThrow(EntityNotFoundException::new);
    }

    private void validateForum(ForumSaveDto dto) {
        if (dto.name().isBlank()) throw new IllegalArgumentException("Name is not valid.");
        if (dto.text().isBlank()) throw new IllegalArgumentException("Text is not valid.");
        if (dto.author().isBlank()) throw new IllegalArgumentException("User email is not valid.");
    }
}
