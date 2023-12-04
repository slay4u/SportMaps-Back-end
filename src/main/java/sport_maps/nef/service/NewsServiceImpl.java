package sport_maps.nef.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import sport_maps.image.dao.NewsImageDao;
import sport_maps.image.domain.NewsImage;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.nef.domain.News;
import sport_maps.security.dao.UserDao;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.nef.dao.NewsDao;
import sport_maps.nef.dto.NewsDto;
import sport_maps.nef.dto.NewsSaveDto;
import sport_maps.security.domain.User;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {
    private final NewsDao newsDao;
    private final NewsImageDao imageDao;
    private final String FOLDER_PATH;
    private final UserDao userDao;
    private final Mapper mapper;

    public NewsServiceImpl(NewsDao newsDao, NewsImageDao imageDao, UserDao userDao, Mapper mapper) throws URISyntaxException {
        this.newsDao = newsDao;
        this.imageDao = imageDao;
        this.FOLDER_PATH = getFOLDER_PATH();
        this.userDao = userDao;
        this.mapper = mapper;
    }

    private String getFOLDER_PATH() throws URISyntaxException {
        URL res = NewsServiceImpl.class.getClassLoader().getResource("images");
        assert res != null;
        File file = Paths.get(res.toURI()).toFile();
        return file.getAbsolutePath();
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
        validateNewsName(dto.name());
        User user = getUser(dto);
        News aNews = mapper.convertToEntity(dto, user, new News());
        newsDao.save(updateContent(aNews, getById(id)));
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

    @Override
    public String uploadImage(MultipartFile file, Long id) throws IOException {
        String filePath = FOLDER_PATH + "\\" + file.getOriginalFilename();
        validatePresentImage(file.getOriginalFilename(), filePath);
        News byId = getById(id);
        NewsImage image = new NewsImage();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setFilePath(filePath);
        image.setNews(byId);
        imageDao.save(image);
        file.transferTo(new File(filePath));
        return "Image uploaded successfully " + file.getOriginalFilename();
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

    private void validatePresentImage(String name, String filePath) {
        Optional<NewsImage> result = imageDao.findByNameAndFilePath(name, filePath);
        if (result.isPresent()) throw new EntityExistsException("Image already exists!");
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
