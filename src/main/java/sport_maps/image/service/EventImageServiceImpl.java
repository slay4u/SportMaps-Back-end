package sport_maps.image.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.commons.service.AbstractService;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.image.dao.EventImageDao;
import sport_maps.image.domain.EventImage;
import sport_maps.nef.dao.EventDao;
import sport_maps.nef.domain.Event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EventImageServiceImpl extends AbstractService<EventImage, EventImageDao, Mapper> implements ImageService {
    private final EventDao eventDao;

    @Override
    @Autowired
    protected void setDao(EventImageDao dao) {
        this.dao = dao;
    }

    @Override
    @Autowired
    protected void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public EventImageServiceImpl(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public void uploadImage(String title, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = IMG_DIR.resolve(fileName);
        EventImage image = mapper.convertToEntity(getEvent(title), file, fileName, String.valueOf(filePath));
        save(image);
        if (!Files.exists(IMG_DIR)) Files.createDirectories(IMG_DIR);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void updateImage(Long id, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = IMG_DIR.resolve(fileName);
        EventImage image = dao.findAllByEventId(id).getFirst();
        Files.deleteIfExists(Path.of(image.getFilePath()));
        save(updateContent(image, fileName, file.getContentType(), String.valueOf(filePath)));
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void deleteAllImages(Long id) throws IOException {
        List<Path> allImagesPaths = dao.findAllByEventId(id).stream().map(EventImage::getFilePath).map(Path::of).toList();
        for (Path filePath : allImagesPaths)
            Files.deleteIfExists(filePath);
    }

    private Event getEvent(String title) {
        return eventDao.findByName(title).orElseThrow(EntityNotFoundException::new);
    }

    private EventImage updateContent(EventImage image, String fileName, String type, String filePath) {
        image.setName(fileName);
        image.setType(type);
        image.setFilePath(filePath);
        return image;
    }
}
