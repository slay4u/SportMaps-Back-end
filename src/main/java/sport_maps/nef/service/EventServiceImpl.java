package sport_maps.nef.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import sport_maps.commons.service.AbstractService;
import sport_maps.image.dao.EventImageDao;
import sport_maps.image.domain.EventImage;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.security.dao.UserDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sport_maps.commons.domain.SportType;
import sport_maps.nef.domain.Event;
import sport_maps.nef.dto.EventDto;
import sport_maps.nef.dto.EventSaveDto;
import sport_maps.nef.dao.EventDao;
import sport_maps.security.domain.User;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@Transactional
public class EventServiceImpl extends AbstractService<Event, EventDao> implements EventService {
    private final EventImageDao imageDao;
    private final String FOLDER_PATH;
    private final UserDao userDao;
    private final Mapper mapper;

    @Override
    @Autowired
    protected void setDao(EventDao eventDao) {
        this.dao = eventDao;
    }

    public EventServiceImpl(EventImageDao imageDao, UserDao userDao, Mapper mapper) throws URISyntaxException {
        this.imageDao = imageDao;
        this.FOLDER_PATH = getFOLDER_PATH();
        this.userDao = userDao;
        this.mapper = mapper;
    }

    private String getFOLDER_PATH() throws URISyntaxException {
        URL res = EventServiceImpl.class.getClassLoader().getResource("images");
        assert res != null;
        File file = Paths.get(res.toURI()).toFile();
        return file.getAbsolutePath();
    }

    @Override
    public void createEvent(EventSaveDto dto) {
        validateEvent(dto);
        validateEventName(dto.name());
        save(mapper.convertToEntity(dto, getSportType(dto), getUser(dto)));
    }

    @Override
    public void updateEvent(Long id, EventSaveDto dto) {
        validateEvent(dto);
        validateEventName(dto.name());
        update(mapper.convertToEntity(dto, getSportType(dto), getUser(dto)), id);
    }

    @Override
    public EventDto getEventById(Long id) {
        return mapper.convertToDto(getById(id));
    }

    @Override
    public void deleteById(Long id) {
        delete(id);
    }

    @Override
    public Page<EventDto> getAllEvents(int page) {
        return getAll(page, size).map(mapper::convertToDto);
    }

    @Override
    public String uploadImage(MultipartFile file, Long id) throws IOException {
        String filePath = FOLDER_PATH + "\\" + file.getOriginalFilename();
        validatePresentImage(file.getOriginalFilename(), filePath);
        Event byId = getById(id);
        EventImage image = new EventImage();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setFilePath(filePath);
        image.setEvent(byId);
        imageDao.save(image);
        file.transferTo(new File(filePath));
        return "Image uploaded successfully " + file.getOriginalFilename();
    }

    private User getUser(EventSaveDto dto) {
        return userDao.findByEmail(dto.author()).orElseThrow(EntityNotFoundException::new);
    }

    private void validateSportType(String sportInString) {
        boolean flag = false;
        for (SportType sportType : SportType.values()) {
            if (sportType.name().equals(sportInString)) {
                flag = true;
                break;
            }
        }
        if (!flag) throw new EntityNotFoundException("Sport type wasn't found.");
    }

    private SportType getSportType(EventSaveDto dto) {
        String sportInString = dto.sportType();
        validateSportType(sportInString);
        return SportType.valueOf(sportInString);
    }

    private void validateEvent(EventSaveDto dto) {
        if (dto.name().isBlank()) throw new IllegalArgumentException("Name is not valid.");
        if (dto.text().isBlank()) throw new IllegalArgumentException("Text is not valid.");
        if (dto.author().isBlank()) throw new IllegalArgumentException("User email is not valid.");
        if (dto.sportType().isBlank()) throw new IllegalArgumentException("Sport type is not valid.");
    }

    private void validateEventName(String name) {
        if (dao.existsByName(name)) throw new EntityExistsException("Event with that name already exists.");
    }

    private void validatePresentImage(String name, String filePath) {
        Optional<EventImage> result = imageDao.findByNameAndFilePath(name, filePath);
        if (result.isPresent()) throw new EntityExistsException("Image already exists!");
    }
}
