package sport_maps.image.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.coach.dao.CoachDao;
import sport_maps.coach.domain.Coach;
import sport_maps.commons.service.AbstractService;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.image.dao.CoachImageDao;
import sport_maps.image.domain.CoachImage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CoachImageServiceImpl extends AbstractService<CoachImage, CoachImageDao, Mapper> implements ImageService {
    private final CoachDao coachDao;

    @Override
    @Autowired
    protected void setDao(CoachImageDao dao) {
        this.dao = dao;
    }

    @Override
    @Autowired
    protected void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    public CoachImageServiceImpl(CoachDao coachDao) {
        this.coachDao = coachDao;
    }

    public void uploadImage(String title, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = IMG_DIR.resolve(fileName);
        CoachImage image = mapper.convertToEntity(getCoach(title.split(":")[0], title.split(":")[1]), file, fileName, String.valueOf(filePath));
        save(image);
        if (!Files.exists(IMG_DIR)) Files.createDirectories(IMG_DIR);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void updateImage(Long id, MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = IMG_DIR.resolve(fileName);
        CoachImage image = dao.findAllByCoachId(id).getFirst();
        Files.deleteIfExists(Path.of(image.getFilePath()));
        save(updateContent(image, fileName, file.getContentType(), String.valueOf(filePath)));
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }

    public void deleteAllImages(Long id) throws IOException {
        List<Path> allImagesPaths = dao.findAllByCoachId(id).stream().map(CoachImage::getFilePath).map(Path::of).toList();
        for (Path filePath : allImagesPaths)
            Files.deleteIfExists(filePath);
    }

    private Coach getCoach(String firstName, String lastName) {
        return coachDao.findByFirstNameAndLastName(firstName, lastName).orElseThrow(EntityNotFoundException::new);
    }

    private CoachImage updateContent(CoachImage image, String fileName, String type, String filePath) {
        image.setName(fileName);
        image.setType(type);
        image.setFilePath(filePath);
        return image;
    }
}
