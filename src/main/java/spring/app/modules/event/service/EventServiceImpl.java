package spring.app.modules.event.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.app.modules.commons.domain.ImageData;
import spring.app.modules.commons.util.convert.SimpleEntityConverter;
import spring.app.modules.event.domain.Event;
import spring.app.modules.event.dto.EventAllInfoDto;
import spring.app.modules.event.dto.EventCreateDto;
import spring.app.modules.commons.exception.AlreadyExistException;
import spring.app.modules.commons.exception.NotFoundException;
import spring.app.modules.event.dao.EventDao;
import spring.app.modules.commons.repository.ImageDataDao;

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
import java.util.stream.Collectors;

@Service
@Transactional
public class EventServiceImpl implements EventService {
    private final int PAGE_ELEMENTS_AMOUNT = 15;
    private final ImageDataDao imageDataDao;
    private final String FOLDER_PATH;
    private final EventDao eventDao;

    public EventServiceImpl(EventDao eventDao, ImageDataDao imageDataDao) throws URISyntaxException {
        this.eventDao = eventDao;
        this.imageDataDao = imageDataDao;
        this.FOLDER_PATH = getFOLDER_PATH();
    }

    private String getFOLDER_PATH() throws URISyntaxException {
        URL res = EventServiceImpl.class.getClassLoader().getResource("images");
        assert res != null;
        File file = Paths.get(res.toURI()).toFile();
        return file.getAbsolutePath();
    }

    @Override
    public int createEvent(EventCreateDto eventDto) {
        validateEvent(eventDto);
        validateEventName(eventDto.getName());
        Event event = SimpleEntityConverter.convert(eventDto, new Event());
        eventDao.save(event);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateEvent(Long id, EventCreateDto eventDto) {
        validateEvent(eventDto);
        Event event = SimpleEntityConverter.convert(eventDto, new Event());
        eventDao.save(updateContent(event, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public EventAllInfoDto getEventById(Long id) {
        Event byId = getById(id);
        return allInfoDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        eventDao.deleteById(id);
    }

    @Override
    public List<EventAllInfoDto> getAllEvents(int pageNumber) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be less than 0!");
        }
        List<Event> events = eventDao.getAllEvents(PageRequest.of(pageNumber, PAGE_ELEMENTS_AMOUNT));
        return listToDto(events);
    }

    @Override
    public String uploadImage(MultipartFile file, Long id) throws IOException {
        String filePath = FOLDER_PATH + "\\" + file.getOriginalFilename();
        validatePresentImage(file.getOriginalFilename(), filePath);
        Event byId = getById(id);

        imageDataDao.save(ImageData
                .builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .event(byId)
                .build()
        );

        file.transferTo(new File(filePath));
        return "Image uploaded successfully " + file.getOriginalFilename();
    }

    @Override
    public double getTotalPagesCount() {
        long count = eventDao.getAllEventCount();
        double pagesNum = (double) count / PAGE_ELEMENTS_AMOUNT;
        return Math.ceil(pagesNum);
    }

    private void validateEvent(EventCreateDto event) {
        if (event.getName().isBlank() || Objects.isNull(event.getName())) {
            throw new IllegalArgumentException("Event's name is not valid");
        }
        if (event.getEventDate().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Event's date is not valid");
        }
        if (event.getSportType().isBlank() || Objects.isNull(event.getSportType())) {
            throw new IllegalArgumentException("Event's sport type is not valid");
        }
    }

    private void validateEventName(String name) {
        Optional<Event> byName = eventDao.getEventByName(name);
        if (byName.isPresent()) {
            throw new IllegalArgumentException("Event with the name "
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

    private Event updateContent(Event event, Event resultEvent) {
        return SimpleEntityConverter.convert(event, resultEvent);
    }

    private Event getById(Long id) {
        Optional<Event> resultEvent = eventDao.getEventById(id);
        if (resultEvent.isEmpty()) {
            throw new NotFoundException("Event by id was not found!");
        }
        return resultEvent.get();
    }

    private byte[] fetchImage(Long id) throws IOException {
        List<ImageData> allByEventId = imageDataDao.findAllByEventId(id);
        if (allByEventId.isEmpty()) {
            return null;
        }
        ImageData singleImage = allByEventId.stream().findFirst().orElseThrow();
        String imagePath = singleImage.getFilePath();
        return Files.readAllBytes(new File(imagePath).toPath());
    }

    public List<EventAllInfoDto> listToDto(List<Event> events) {
        return events.stream().map(this::allInfoDto).collect(Collectors.toList());
    }

    public EventAllInfoDto allInfoDto(Event event) {
        EventAllInfoDto eventAllInfoDto = SimpleEntityConverter.convert(event, new EventAllInfoDto());
        try {
            eventAllInfoDto.setImage(fetchImage(event.getIdEvent()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error while reading from image path! " + event.getIdEvent());
        }
        return eventAllInfoDto;
    }
}
