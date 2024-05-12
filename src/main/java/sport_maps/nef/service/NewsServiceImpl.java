package sport_maps.nef.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.commons.service.AbstractService;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.nef.dao.NewsDao;
import sport_maps.nef.domain.News;
import sport_maps.nef.dto.NewsDto;
import sport_maps.nef.dto.NewsSaveDto;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.User;

@Service
@Transactional
public class NewsServiceImpl extends AbstractService<News, NewsDao, Mapper> implements NewsService {
    private final UserDao userDao;

    @Override
    @Autowired
    protected void setDao(NewsDao newsDao) {
        this.dao = newsDao;
    }

    @Override
    @Autowired
    protected void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public NewsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void createNews(NewsSaveDto dto) {
        validateNews(dto);
        validateNewsName(dto.name());
        save(mapper.convertToEntity(dto, getUser(dto)));
    }

    @Override
    public void updateNews(Long id, NewsSaveDto dto) {
        validateNews(dto);
        if (!getById(id).getName().contentEquals(dto.name())) validateNewsName(dto.name());
        update(mapper.convertToEntity(dto, getUser(dto)), id);
    }

    @Override
    public NewsDto getNewsById(Long id) {
        return mapper.convertToDto(getById(id));
    }

    @Override
    public void deleteById(Long id) {
        delete(id);
    }

    @Override
    public Page<NewsDto> getAllNews(int page) {
        return getAll(page, size).map(mapper::convertToDto);
    }

    private User getUser(NewsSaveDto dto) {
        return userDao.findByEmail(dto.author()).orElseThrow(EntityNotFoundException::new);
    }

    private void validateNews(NewsSaveDto dto) {
        if (dto.name().isBlank()) throw new IllegalArgumentException("Name is not valid.");
        if (dto.text().isBlank()) throw new IllegalArgumentException("Text is not valid.");
        if (dto.author().isBlank()) throw new IllegalArgumentException("User email is not valid.");
    }

    private void validateNewsName(String name) {
        if (dao.existsByName(name)) throw new EntityExistsException("News with that name already exists.");
    }
}
