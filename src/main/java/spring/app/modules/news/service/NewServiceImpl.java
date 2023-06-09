package spring.app.modules.news.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.app.modules.comments.newsComment.dao.NewsCommentDao;
import spring.app.modules.comments.newsComment.domain.NewsComment;
import spring.app.modules.commons.domain.ImageData;
import spring.app.modules.commons.exception.AlreadyExistException;
import spring.app.modules.commons.exception.NotFoundException;
import spring.app.modules.commons.repository.ImageDataDao;
import spring.app.modules.news.dao.NewDao;
import spring.app.modules.news.domain.New;
import spring.app.modules.news.dto.NewAllInfoDto;
import spring.app.modules.news.dto.NewCreateDto;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class NewServiceImpl implements NewService, NewGeneralHandler {
    private final int PAGE_ELEMENTS_AMOUNT = 15;
    private final NewDao newDao;
    private final ImageDataDao imageDataDao;
    private final String FOLDER_PATH;
    private final NewsCommentDao newsCommentDao;

    public NewServiceImpl(NewDao newDao, ImageDataDao imageDataDao, NewsCommentDao newsCommentDao) throws URISyntaxException {
        this.newDao = newDao;
        this.imageDataDao = imageDataDao;
        this.FOLDER_PATH = getFOLDER_PATH();
        this.newsCommentDao = newsCommentDao;
    }

    private String getFOLDER_PATH() throws URISyntaxException {
        URL res = NewServiceImpl.class.getClassLoader().getResource("images");
        assert res != null;
        File file = Paths.get(res.toURI()).toFile();
        return file.getAbsolutePath();
    }

    @Override
    public int createNew(NewCreateDto newDto) {
        validateNew(newDto);
        validateNewName(newDto.getName());
        New aNew = convertToEntity(newDto, new New());
        newDao.save(aNew);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateNew(Long id, NewCreateDto newDto) {
        validateNew(newDto);
        New aNew = convertToEntity(newDto, new New());
        newDao.save(updateContent(aNew, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public NewAllInfoDto getNewById(Long id) {
        New byId = getById(id);
        return allInfoDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        newDao.deleteById(id);
    }

    @Override
    public List<NewAllInfoDto> getAllNews(int pageNumber) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be less than 0!");
        }
        List<New> news = newDao.getAllNews(PageRequest.of(pageNumber, PAGE_ELEMENTS_AMOUNT));
        return listToDto(news);
    }

    @Override
    public String uploadImage(MultipartFile file, Long id) throws IOException {
        String filePath = FOLDER_PATH + "\\" + file.getOriginalFilename();
        validatePresentImage(file.getOriginalFilename(), filePath);
        New byId = getById(id);

        imageDataDao.save(ImageData
                .builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .aNew(byId)
                .build()
        );

        file.transferTo(new File(filePath));
        return "Image uploaded successfully " + file.getOriginalFilename();
    }

    @Override
    public double getTotalPagesCount() {
        long count = newDao.getAllNewCount();
        double pagesNum = (double) count / PAGE_ELEMENTS_AMOUNT;
        return Math.ceil(pagesNum);
    }

    private void validateNew(NewCreateDto aNew) {
        if (aNew.getName().isBlank() || Objects.isNull(aNew.getName())) {
            throw new IllegalArgumentException("New's name is not valid");
        }
        if (aNew.getPublishDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Publish date is not valid");
        }
        if (aNew.getDesc().isBlank() || Objects.isNull(aNew.getDesc())) {
            throw new IllegalArgumentException("Description is not valid");
        }
    }

    private void validateNewName(String name) {
        Optional<New> byName = newDao.getNewByName(name);
        if (byName.isPresent()) {
            throw new IllegalArgumentException("New with the name "
                    + name +
                    " already exists!");
        }
    }

    private void validatePresentImage(String name, String filePath) {
        Optional<ImageData> result = imageDataDao.findByNameAndFilePath(name, filePath);
        if (result.isPresent()) {
            throw new AlreadyExistException("Image already exists!");
        }
    }

    private New updateContent(New aNew, New resultNew) {
        resultNew.setName(aNew.getName());
        resultNew.setPublishDate(aNew.getPublishDate());
        resultNew.setDescription(aNew.getDescription());
        return resultNew;
    }

    private New getById(Long id) {
        Optional<New> resultNew = newDao.getNewById(id);
        if (resultNew.isEmpty()) {
            throw new NotFoundException("New by id was not found!");
        }
        return resultNew.get();
    }

    private New convertToEntity(NewCreateDto newDto, New aNew) {
        aNew.setName(newDto.getName());
        aNew.setPublishDate(newDto.getPublishDate());
        aNew.setDescription(newDto.getDesc());
        return aNew;
    }

    private byte[] fetchImage(Long id) throws IOException {
        List<ImageData> allByNewId = imageDataDao.findAllByANewId(id);
        if (allByNewId.isEmpty()) {
            return null;
        }
        ImageData singleImage = allByNewId.stream().findFirst().orElseThrow();
        String imagePath = singleImage.getFilePath();
        return Files.readAllBytes(new File(imagePath).toPath());
    }

    private List<NewsComment> fetchNewsComments(Long id) {
        List<NewsComment> comments = newsCommentDao.findAllByNewsId(id);
        if (comments.isEmpty()) {
            return null;
        }
        return comments;
    }

    @Override
    public List<NewAllInfoDto> listToDto(List<New> news) {
        return NewGeneralHandler.super.listToDto(news);
    }

    @Override
    public NewAllInfoDto allInfoDto(New aNew) {
        NewAllInfoDto newAllInfoDto = NewGeneralHandler.super.allInfoDto(aNew);
        try {
            newAllInfoDto.setImage(fetchImage(aNew.getIdNew()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error while reading from image path! " + aNew.getIdNew());
        }
        newAllInfoDto.setCommentSet(fetchNewsComments(aNew.getIdNew()));
        return newAllInfoDto;
    }
}
