package sport_maps.nef.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import sport_maps.image.dao.EventImageDao;
import sport_maps.image.domain.EventImage;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.security.dao.UserDao;
import org.springframework.data.domain.PageRequest;
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
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class EventServiceImpl implements EventService {
    private final EventImageDao imageDao;
    private final String FOLDER_PATH;
    private final EventDao eventDao;
    private final UserDao userDao;
    private final Mapper mapper;

    public EventServiceImpl(EventDao eventDao, EventImageDao imageDao, UserDao userDao, Mapper mapper) throws URISyntaxException {
        this.eventDao = eventDao;
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
        User user = getUser(dto);
        SportType sportType = getSportType(dto);
        Event event = mapper.convertToEntity(dto, sportType, user, new Event());
        eventDao.save(event);
    }

    @Override
    public void updateEvent(Long id, EventSaveDto dto) {
        validateEvent(dto);
        validateEventName(dto.name());
        User user = getUser(dto);
        SportType sportType = getSportType(dto);
        Event event = mapper.convertToEntity(dto, sportType, user, new Event());
        eventDao.save(updateContent(event, getById(id)));
    }

    @Override
    public EventDto getEventById(Long id) {
        Event byId = getById(id);
        return mapper.toEventDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        eventDao.deleteById(id);
    }

    @Override
    public Page<EventDto> getAllEvents(int page) {
        return eventDao.findAll(PageRequest.of(page, size)).map(mapper::toEventDto);
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
        return userDao.findByEmail(dto.author()).orElseThrow(() -> new EntityNotFoundException("User wasn't found."));
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
        if (dto.sportType().isBlank()) {
            throw new IllegalArgumentException("Sport type is not valid.");
        }
    }

    private void validateEventName(String name) {
        Optional<Event> byName = eventDao.findByName(name);
        if (byName.isPresent()) throw new EntityExistsException("Event with that name already exists.");
    }

    private void validatePresentImage(String name, String filePath) {
        Optional<EventImage> result = imageDao.findByNameAndFilePath(name, filePath);
        if (result.isPresent()) throw new EntityExistsException("Image already exists!");
    }

    private Event updateContent(Event event, Event resultEvent) {
        resultEvent.setText(event.getText());
        resultEvent.setName(event.getName());
        resultEvent.setSportType(event.getSportType());
        resultEvent.setDate(event.getDate());
        return resultEvent;
    }

    private Event getById(Long id) {
        return eventDao.findById(id).orElseThrow(() -> new EntityNotFoundException("Event wasn't found."));
    }
}
