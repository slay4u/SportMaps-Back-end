package sport_maps.image.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.image.dao.NewsImageDao;
import sport_maps.image.domain.NewsImage;
import sport_maps.nef.dao.NewsDao;
import sport_maps.nef.domain.News;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ImageDataServiceImpl implements ImageDataService {
    private final NewsImageDao newsImageDao;
    private final NewsDao newsDao;
    private final Mapper mapper;

    public ImageDataServiceImpl(NewsImageDao newsImageDao, NewsDao newsDao, Mapper mapper) {
        this.newsImageDao = newsImageDao;
        this.newsDao = newsDao;
        this.mapper = mapper;
    }

    public void uploadImage(String title, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = IMG_DIR.resolve(fileName);

        NewsImage image = mapper.convertToEntity(getNew(title), file, fileName, String.valueOf(filePath));
        newsImageDao.save(image);

        if (!Files.exists(IMG_DIR)) Files.createDirectories(IMG_DIR);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void updateImage(Long id, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = IMG_DIR.resolve(fileName);

        NewsImage image = newsImageDao.findAllByNewsId(id).getFirst();
        Files.deleteIfExists(Path.of(image.getFilePath()));
        newsImageDao.save(updateContent(image, fileName, file.getContentType(), String.valueOf(filePath)));

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void deleteAllImages(Long id) throws IOException {
        List<Path> allImagesPaths = newsImageDao.findAllByNewsId(id).stream().map(NewsImage::getFilePath).map(Path::of).toList();
        for (Path filePath : allImagesPaths)
            Files.deleteIfExists(filePath);
    }

    private News getNew(String title) {
        return newsDao.findByName(title).orElseThrow(() -> new EntityNotFoundException("News wasn't found."));
    }

    private NewsImage updateContent(NewsImage image, String fileName, String type, String filePath) {
        image.setName(fileName);
        image.setType(type);
        image.setFilePath(filePath);
        return image;
    }
}
