package sport_maps.coach.service;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sport_maps.coach.domain.Coach;
import sport_maps.commons.service.AbstractService;
import sport_maps.coach.dto.CoachDto;
import sport_maps.coach.dto.CoachSaveDto;
import sport_maps.commons.domain.SportType;
import sport_maps.coach.dao.CoachDao;
import sport_maps.commons.util.mapper.Mapper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class CoachServiceImpl extends AbstractService<Coach, CoachDao, Mapper> implements CoachService {
    @Override
    @Autowired
    protected void setDao(CoachDao dao) {
        this.dao = dao;
    }

    @Override
    @Autowired
    protected void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void createCoach(CoachSaveDto dto) {
        validateCoach(dto);
        save(mapper.convertToEntity(dto, getSportType(dto)));
    }

    @Override
    public void updateCoach(Long id, CoachSaveDto dto) {
        validateCoach(dto);
        update(mapper.convertToEntity(dto, getSportType(dto)), id);
    }

    @Override
    public CoachDto getCoachById(Long id) {
        return mapper.convertToDto(getById(id));
    }

    @Override
    public void deleteById(Long id) {
        delete(id);
    }

    @Override
    public Page<CoachDto> getAllCoaches(int page) {
        return getAll(page, size).map(mapper::convertToDto);
    }

    private void validateCoach(CoachSaveDto dto) {
        if (dto.firstName().isBlank() || isValidCoachName(dto.firstName())) throw new IllegalArgumentException("Coach's first name is not valid");
        if (dto.lastName().isBlank() || isValidCoachName(dto.lastName())) throw new IllegalArgumentException("Coach's last name is not valid");
        if (dto.sportType().isBlank()) throw new IllegalArgumentException("Coach's sport type is not valid");
        if (dto.age() == null || dto.age() < 18) throw new IllegalArgumentException("Coach's age is not valid");
        if (dto.experience() == null || dto.experience() <= 0) throw new IllegalArgumentException("Coach's experience is not valid");
        if (dto.price() == null || dto.price() <= 0) throw new IllegalArgumentException("Coach's price is not valid");
        if (dto.description().isBlank()) throw new IllegalArgumentException("Coach's description is not valid");
        if (dao.existsByFirstNameAndLastNameAndAgeAndExperienceAndPriceAndSportType(dto.firstName(), dto.lastName(), dto.age(), dto.experience(),
                dto.price(), SportType.valueOf(dto.sportType()))) throw new EntityExistsException("Coach with those fields already exists.");
    }

    private boolean isValidCoachName(String name) {
        String regex = "^(?=.{2,30}$)[A-Z][a-zA-Z]*(?:\\h+[A-Z][a-zA-Z]*)*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(name);
        return !m.matches();
    }

    private void validateSportType(String sport) {
        boolean flag = false;
        for (SportType sportType : SportType.values()) {
            if (sportType.name().equals(sport)) {
                flag = true;
                break;
            }
        }
        if (!flag) throw new EntityNotFoundException("Sport type wasn't found.");
    }

    private SportType getSportType(CoachSaveDto dto) {
        String sport = dto.sportType();
        validateSportType(sport);
        return SportType.valueOf(sport);
    }
}
