package spring.app.modules.coach.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.app.modules.coach.domain.Coach;
import spring.app.modules.commons.domain.ImageData;
import spring.app.modules.coach.dto.CoachAllInfoDto;
import spring.app.modules.coach.dto.CoachCreateDto;
import spring.app.modules.commons.domain.SportType;
import spring.app.modules.commons.exception.AlreadyExistException;
import spring.app.modules.commons.exception.NotFoundException;
import spring.app.modules.coach.dao.CoachDao;
import spring.app.modules.commons.repository.ImageDataDao;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class CoachServiceImpl implements CoachService, CoachGeneralHandler {
    private final int PAGE_ELEMENTS_AMOUNT = 15;
    private final ImageDataDao imageDataDao;
    private final String FOLDER_PATH;
    private final CoachDao coachDao;

    public CoachServiceImpl(ImageDataDao imageDataDao, CoachDao coachDao) throws URISyntaxException {
        this.coachDao = coachDao;
        this.FOLDER_PATH = getFOLDER_PATH();
        this.imageDataDao = imageDataDao;
    }

    private String getFOLDER_PATH() throws URISyntaxException {
        URL res = CoachServiceImpl.class.getClassLoader().getResource("images");
        assert res != null;
        File file = Paths.get(res.toURI()).toFile();
        return file.getAbsolutePath();
    }

    @Override
    public int createCoach(CoachCreateDto coachDto) {
        validateCoach(coachDto);
        validatePresentCoach(coachDto);
        SportType sportType = getSportType(coachDto);
        Coach coach = convertToEntity(coachDto, sportType, new Coach());
        coachDao.save(coach);
        return HttpStatus.CREATED.value();
    }

    @Override
    public int updateCoach(Long id, CoachCreateDto coachDto) {
        validateCoach(coachDto);
        SportType sportType = getSportType(coachDto);
        Coach coach = convertToEntity(coachDto, sportType, new Coach());
        coachDao.save(updateContent(coach, getById(id)));
        return HttpStatus.CREATED.value();
    }

    @Override
    public CoachAllInfoDto getCoachById(Long id) {
        Coach byId = getById(id);
        return allInfoDto(byId);
    }

    @Override
    public void deleteById(Long id) {
        getById(id);
        coachDao.deleteById(id);
    }

    @Override
    public List<CoachAllInfoDto> getAllCoaches(int pageNumber) {
        if (pageNumber < 0) {
            throw new IllegalArgumentException("Page number cannot be less than 0!");
        }
        List<Coach> coaches = coachDao.getAllCoaches(PageRequest.of(pageNumber, PAGE_ELEMENTS_AMOUNT));
        return listToDto(coaches);
    }

    public String uploadImage(MultipartFile file, Long id) throws IOException {
        String filePath = FOLDER_PATH + "\\" + file.getOriginalFilename();
        validatePresentImage(file.getOriginalFilename(), filePath);
        Coach byId = getById(id);

        imageDataDao.save(ImageData
                .builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath)
                .coach(byId)
                .build()
        );

        file.transferTo(new File(filePath));
        return "Image uploaded successfully " + file.getOriginalFilename();
    }

    @Override
    public double getTotalPagesCount() {
        long count = coachDao.getAllCoachCount();
        double pagesNum = (double) count / PAGE_ELEMENTS_AMOUNT;
        return Math.ceil(pagesNum);
    }

    private void validateCoach(CoachCreateDto coach) {
        if (coach.getFirstName().isBlank() || Objects.isNull(coach.getFirstName())
                || isValidCoachName(coach.getFirstName())) {
            throw new IllegalArgumentException("Coach's first name is not valid");
        }
        if (coach.getLastName().isBlank() || Objects.isNull(coach.getLastName())
                || isValidCoachName(coach.getLastName())) {
            throw new IllegalArgumentException("Coach's last name is not valid");
        }
        if (coach.getSportType().isBlank() || Objects.isNull(coach.getSportType())) {
            throw new IllegalArgumentException("Coach's sport type is not valid");
        }
        if (coach.getAge() == null || Objects.isNull(coach.getAge()) || coach.getAge() < 18) {
            throw new IllegalArgumentException("Coach's age is not valid");
        }
        if (coach.getExperience() == null || Objects.isNull(coach.getExperience()) || coach.getExperience() < 0) {
            throw new IllegalArgumentException("Coach's experience is not valid");
        }
        if (coach.getPrice() == null || Objects.isNull(coach.getPrice()) || coach.getPrice() < 0) {
            throw new IllegalArgumentException("Coach's price is not valid");
        }
        if (coach.getDesc().isBlank() || Objects.isNull(coach.getDesc())) {
            throw new IllegalArgumentException("Coach's description is not valid");
        }
    }

    private boolean isValidCoachName(String name) {
        String regex = "^(?=.{2,30}$)[A-Z][a-zA-Z]*(?:\\h+[A-Z][a-zA-Z]*)*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);
        return !m.matches();
    }

    private void validatePresentCoach(CoachCreateDto coachDto) {
        String firstName = coachDto.getFirstName();
        String lastName = coachDto.getLastName();
        Long age = coachDto.getAge();
        Long experience = coachDto.getExperience();
        Double price = coachDto.getPrice();
        String sportType = coachDto.getSportType();

        Optional<Coach> result = coachDao
                .getCoachByAllFields(firstName, lastName, age, experience, price, sportType);

        if(result.isPresent()) {
            String body = """
          {
              "message": "The coach already exists!",
              "firstName": "%s",
              "lastName": "%s",
              "age": "%s",
              "experience": "%s",
              "price": "%s",
              "sportType": "%s"
          }
          """.formatted(firstName, lastName, age, experience, price, sportType);
            throw new AlreadyExistException(body);
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

    private SportType getSportType(CoachCreateDto coach) {
        String sportInString = coach.getSportType();
        validateSportType(sportInString);
        return SportType.valueOf(sportInString);
    }

    private void validatePresentImage(String name, String filePath) {
        Optional<ImageData> result = imageDataDao.findByNameAndFilePath(name, filePath);
        if (result.isPresent()) {
            throw new AlreadyExistException("Image already exists!");
        }
    }

    private Coach updateContent(Coach coach, Coach resultCoach) {
        resultCoach.setFirstName(coach.getFirstName());
        resultCoach.setLastName(coach.getLastName());
        resultCoach.setAge(coach.getAge());
        resultCoach.setPrice(coach.getPrice());
        resultCoach.setExperience(coach.getExperience());
        resultCoach.setDescription(coach.getDescription());
        resultCoach.setSportType(coach.getSportType());
        return resultCoach;
    }

    private Coach getById(Long id) {
        Optional<Coach> resultCoach = coachDao.getCoachById(id);
        if (resultCoach.isEmpty()) {
            throw new NotFoundException("Coach by id was not found!");
        }
        return resultCoach.get();
    }

    private Coach convertToEntity(CoachCreateDto coachDto, SportType sportType,
                                  Coach coach) {
        coach.setFirstName(coachDto.getFirstName());
        coach.setLastName(coachDto.getLastName());
        coach.setAge(coachDto.getAge());
        coach.setPrice(coachDto.getPrice());
        coach.setExperience(coachDto.getExperience());
        coach.setDescription(coachDto.getDesc());
        coach.setSportType(sportType);
        return coach;
    }

    private byte[] fetchImage(Long id) throws IOException {
        List<ImageData> allByCoachId = imageDataDao.findAllByCoachId(id);
        if (allByCoachId.isEmpty()) {
            return null;
        }
        ImageData singleImage = allByCoachId.stream().findFirst().orElseThrow();
        String imagePath = singleImage.getFilePath();
        return Files.readAllBytes(new File(imagePath).toPath());
    }

    @Override
    public List<CoachAllInfoDto> listToDto(List<Coach> coaches) {
        return CoachGeneralHandler.super.listToDto(coaches);
    }

    @Override
    public CoachAllInfoDto allInfoDto(Coach coach) {
        CoachAllInfoDto coachAllInfoDto = CoachGeneralHandler.super.allInfoDto(coach);
        try {
            coachAllInfoDto.setImage(fetchImage(coach.getIdCoach()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Error while reading from image path! " + coach.getIdCoach());
        }
        return coachAllInfoDto;
    }
}
