package spring.app.modules.coach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import spring.app.modules.coach.dao.CoachDao;
import spring.app.modules.coach.dao.CoachImageDataDao;
import spring.app.modules.coach.domain.Coach;
import spring.app.modules.coach.domain.ImageData;
import spring.app.modules.coach.dto.CoachAllInfoDto;
import spring.app.modules.coach.dto.CoachCreateDto;
import spring.app.modules.commons.exception.SMBusinessLogicException;
import spring.app.modules.commons.other.ErrorMap;
import spring.app.modules.commons.service.BaseService;
import spring.app.modules.commons.validation.PersonDtoValidator;
import spring.app.modules.imgs.service.BaseImageDataService;

import java.util.List;

@Service
public class CoachServiceImpl extends BaseService<Coach, Long, CoachDao> implements CoachService {

    private final BaseImageDataService<ImageData, Long, CoachImageDataDao> imageService;
    private final CoachDao coachDao;
    private final PersonDtoValidator<CoachCreateDto> coachDtoValidator;
    private final ErrorMap errors;

    @Autowired
    @SuppressWarnings("all")
    public CoachServiceImpl(CoachDao coachDao, BaseImageDataService imageService, PersonDtoValidator coachDtoValidator, ErrorMap errors) {
        super(coachDao);
        this.coachDao = coachDao;
        this.imageService = imageService;
        this.coachDtoValidator = coachDtoValidator;
        this.errors = errors;
    }

    @Override
    public int createCoach(CoachCreateDto coachDto) {
        // Too easy, right?
        validateDto(coachDto);
        return CREATE(coachDto, new Coach());
    }

    @Override
    public int updateCoach(Long id, CoachCreateDto coachDto) {
        validateDto(coachDto);
        return UPDATE(coachDto, new Coach(), id);
    }

    @Override
    public CoachAllInfoDto getCoachById(Long id) {
        CoachAllInfoDto coachAllInfoDto = READ_ID(id, CoachAllInfoDto.class);
        coachAllInfoDto.setImage(imageService.downloadImage(id));
        return coachAllInfoDto;
    }

    @Override
    public void deleteById(Long id) {
        DELETE(id);
    }

    @Override
    public List<CoachAllInfoDto> getAllCoaches(int page) {
        return READ_ALL(page, CoachAllInfoDto.class);
    }

    @Override
    public String uploadImage(MultipartFile file, Long id) {
        return null;
    }

    public double getTotalPagesCount() {
        return getPagesNum(coachDao.countEverything());
    }

    private void validateDto(CoachCreateDto coachDto) {
        if (!coachDtoValidator.validate(coachDto, errors).isEmpty()) {
            throw new SMBusinessLogicException(errors);
        }
    }
}
