package sport_maps.nef.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.image.service.ImageDataServiceImpl;
import sport_maps.nef.dao.NewsDao;
import sport_maps.nef.domain.News;
import sport_maps.nef.dto.NewsDto;
import sport_maps.nef.dto.NewsSaveDto;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {
    private final NewsDao newsDao;
    private final UserDao userDao;
    private final Mapper mapper;

    public NewsServiceImpl(NewsDao newsDao, UserDao userDao, Mapper mapper) {
        this.newsDao = newsDao;
        this.userDao = userDao;
        this.mapper = mapper;
    }

    @Override
    public void createNews(NewsSaveDto dto) {
        validateNews(dto);
        validateNewsName(dto.name());
        User user = getUser(dto);
        News aNews = mapper.convertToEntity(dto, user, new News());
        newsDao.save(aNews);
    }

    @Override
    public void updateNews(Long id, NewsSaveDto dto) {
        validateNews(dto);
        News prevNews = getById(id);
        if (!prevNews.getName().contentEquals(dto.name())) validateNewsName(dto.name());
        User user = getUser(dto);
        News aNews = mapper.convertToEntity(dto, user, new News());
        newsDao.save(updateContent(aNews, prevNews));
    }

    @Override
    public NewsDto getNewsById(Long id) {
        News byId = getById(id);
        return mapper.toNewsDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        newsDao.deleteById(id);
    }

    @Override
    public Page<NewsDto> getAllNews(int page) {
        return newsDao.findAll(PageRequest.of(page, size)).map(mapper::toNewsDto);
    }

    private User getUser(NewsSaveDto dto) {
        return userDao.findByEmail(dto.author()).orElseThrow(() -> new EntityNotFoundException("User wasn't found."));
    }

    private void validateNews(NewsSaveDto dto) {
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

    private void validateNewsName(String name) {
        Optional<News> byName = newsDao.findByName(name);
        if (byName.isPresent()) throw new EntityExistsException("News with that name already exists.");
    }

    private News updateContent(News news, News resultNews) {
        resultNews.setName(news.getName());
        resultNews.setDate(news.getDate());
        resultNews.setText(news.getText());
        return resultNews;
    }

    private News getById(Long id) {
        return newsDao.findById(id).orElseThrow(() -> new EntityNotFoundException("News wasn't found."));
    }
}
