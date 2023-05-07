package spring.app.service.impl;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.app.domain.ImageData;
import spring.app.domain.Event;
import spring.app.domain.SportType;
import spring.app.dto.events.EventAllInfoDto;
import spring.app.dto.events.EventCreateDto;
import spring.app.dto.events.EventInfoDto;
import spring.app.exception.AlreadyExistException;
import spring.app.exception.NotFoundException;
import spring.app.repository.EventDao;
import spring.app.repository.ImageDataDao;
import spring.app.service.EventService;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Transactional
public class EventServiceImpl implements EventService, EventGeneralHandler {
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
    public EventInfoDto createEvent(EventCreateDto eventDto) {
        validateEvent(eventDto);
        validateEventName(eventDto.getName());
        SportType sportType = getSportType(eventDto);
        Event event = convertToEntity(eventDto, sportType, new Event());
        Event savedEvent = eventDao.save(event);
        return convertEntityToDto(savedEvent, "created");
    }

    @Override
    public EventInfoDto updateEvent(Long id, EventCreateDto eventDto) {
        validateEvent(eventDto);
        SportType sportType = getSportType(eventDto);
        Event event = convertToEntity(eventDto, sportType, new Event());
        Event savedEvent = eventDao.save(updateContent(event, getById(id)));
        return convertEntityToDto(savedEvent, "updated");
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
    public byte[] downloadImages(Long id) throws IOException {
        List<ImageData> imageData = imageDataDao.findAllByEventId(id);
        if (imageData.isEmpty()) {
            throw new NotFoundException("No image by the id " + id + " has been found!");
        }
        return zipImages(imageData.stream().map(ImageData::getFilePath).toList());
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
        if (event.getSportType() == null || Objects.isNull(event.getSportType())) {
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

    private void validateSportType(String sportInString) {
        boolean flag = false;
        for (SportType sportType : SportType.values()) {
            if (sportType.name().equals(sportInString)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            throw new IllegalArgumentException("Sport type was not found!");
        }
    }

    private SportType getSportType(EventCreateDto event) {
        String sportInString = event.getSportType();
        validateSportType(sportInString);
        return SportType.valueOf(sportInString);
    }

    private void validatePresentImage(String name, String filePath) {
        Optional<ImageData> result = imageDataDao.findByNameAndFilePath(name, filePath);
        if (result.isPresent()) {
            throw new AlreadyExistException("Image already exists!");
        }
    }

    private Event updateContent(Event event, Event resultEvent) {
        resultEvent.setName(event.getName());
        resultEvent.setEventDate(event.getEventDate());
        resultEvent.setSportType(event.getSportType());
        return resultEvent;
    }

    private Event getById(Long id) {
        Optional<Event> resultEvent = eventDao.getEventById(id);
        if (resultEvent.isEmpty()) {
            throw new NotFoundException("Event by id was not found!");
        }
        return resultEvent.get();
    }

    private Event convertToEntity(EventCreateDto eventDto, SportType sportType,
                                  Event event) {
        event.setName(eventDto.getName());
        event.setEventDate(eventDto.getEventDate());
        event.setDescription(eventDto.getDesc());
        event.setSportType(sportType);
        return event;
    }

    private EventInfoDto convertEntityToDto(Event event, String state) {
        return EventInfoDto.builder()
                .id(event.getIdEvent())
                .result("Event " + event.getName() + " " + state + " successfully")
                .build();
    }

    private byte[] zipImages(List<String> imgPathList) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);

        List<File> fileList = new ArrayList<>();

        for (String imgPath : imgPathList) {
            fileList.add(new File(imgPath));
        }

        for (File file : fileList) {
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream;
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                throw new NotFoundException("Image with path "
                        + file.getPath() + " has not been found!");
            }

            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }

        zipOutputStream.finish();
        zipOutputStream.flush();
        IOUtils.closeQuietly(zipOutputStream);
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
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

    @Override
    public List<EventAllInfoDto> listToDto(List<Event> events) {
        return EventGeneralHandler.super.listToDto(events);
    }

    @Override
    public EventAllInfoDto allInfoDto(Event event) {
        EventAllInfoDto eventAllInfoDto = EventGeneralHandler.super.allInfoDto(event);
        try {
            eventAllInfoDto.setImage(fetchImage(event.getIdEvent()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error while reading from image path! " + event.getIdEvent());
        }
        return eventAllInfoDto;
    }
}
