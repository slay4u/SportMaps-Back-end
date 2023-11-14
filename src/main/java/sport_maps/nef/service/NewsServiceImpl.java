package sport_maps.nef.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import sport_maps.commons.dao.NewsImageDao;
import sport_maps.commons.domain.NewsImage;
import sport_maps.commons.util.mapper.CustomObjectMapper;
import sport_maps.nef.domain.News;
import sport_maps.security.dao.UserDao;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NewsServiceImpl implements NewsService {
    private final int PAGE_ELEMENTS_AMOUNT = 15;
    private final NewsDao newsDao;
    private final NewsImageDao imageDao;
    private final String FOLDER_PATH;
    private final UserDao userDao;
    private final CustomObjectMapper mapper;

    public NewsServiceImpl(NewsDao newsDao, NewsImageDao imageDao, UserDao userDao,
                           CustomObjectMapper mapper) throws URISyntaxException {
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
    public int createNew(NewsSaveDto dto) {
        validateNew(dto);
        validateNewName(dto.name());
        User user = getUser(dto);
        News aNews = mapper.convertToEntity(dto, user, new News());
        newsDao.save(aNews);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateNew(Long id, NewsSaveDto dto) {
        validateNew(dto);
        User user = getUser(dto);
        News aNews = mapper.convertToEntity(dto, user, new News());
        newsDao.save(updateContent(aNews, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public NewsDto getNewById(Long id) {
        News byId = getById(id);
        return mapper.toNewsDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        newsDao.deleteById(id);
    }

    @Override
    public List<NewsDto> getAllNews(int pageNumber) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be less than 0!");
        }
        List<News> news = newsDao.findAll(PageRequest.of(pageNumber, PAGE_ELEMENTS_AMOUNT)).getContent();
        return mapper.toListNewsDto(news);
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

    @Override
    public double getTotalPagesCount() {
        long count = newsDao.getAllNewCount();
        double pagesNum = (double) count / PAGE_ELEMENTS_AMOUNT;
        return Math.ceil(pagesNum);
    }

    private User getUser(NewsSaveDto dto) {
        return userDao.findByEmail(dto.createdBy()).orElseThrow(() ->
                new EntityNotFoundException("User by email " + dto.createdBy() + " was not found."));
    }

    private void validateNew(NewsSaveDto dto) {
        if (dto.name().isBlank()) {
            throw new IllegalArgumentException("News's name is not valid");
        }
        if (dto.date().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Publish date is not valid");
        }
        if (dto.text().isBlank()) {
            throw new IllegalArgumentException("Description is not valid");
        }
        if (dto.createdBy().isBlank()) {
            throw new IllegalArgumentException("User email is not valid");
        }
    }

    private void validateNewName(String name) {
        Optional<News> byName = newsDao.findNewsByName(name);
        if (byName.isPresent()) {
            throw new IllegalArgumentException("News with the name "
                    + name +
                    " already exists!");
        }
    }

    private void validatePresentImage(String name, String filePath) {
        Optional<NewsImage> result = imageDao.findNewsImageByNameAndFilePath(name, filePath);
        if (result.isPresent()) {
            throw new EntityExistsException("Image already exists!");
        }
    }

    private News updateContent(News news, News resultNews) {
        resultNews.setName(news.getName());
        resultNews.setDate(news.getDate());
        resultNews.setText(news.getText());
        return resultNews;
    }

    private News getById(Long id) {
        Optional<News> resultNew = newsDao.findById(id);
        if (resultNew.isEmpty()) {
            throw new EntityNotFoundException("News by id was not found!");
        }
        return resultNew.get();
    }
}
