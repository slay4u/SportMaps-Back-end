package spring.app.modules.coach.validation;

import org.springframework.stereotype.Service;
import spring.app.modules.coach.dto.CoachCreateDto;
import spring.app.modules.commons.other.ErrorMap;
import spring.app.modules.commons.validation.PersonDtoValidator;

@Service
public class CoachDtoValidator extends PersonDtoValidator<CoachCreateDto> {

    public ErrorMap validate(CoachCreateDto dto, ErrorMap errors) {
        validateAge(dto, errors);
        validateExperience(dto, errors);
        return errors;
    }

    @Override
    public void validateAge(CoachCreateDto dto, ErrorMap errors) {
        /*if (dto.getDob() != null) {
            int fullYears = Period.between(dto.getDob(), LocalDate.now()).getYears();
            Map<SportType, Long> sports = dto.getSports();
            for (Map.Entry<SportType, Long> entry: sports.entrySet()) {
                SportType sportType = entry.getKey();
                if (sportType.getMinCoachAge() > fullYears || sportType.getMaxCoachAge() < fullYears) {
                    errors.put("age", sportType.name(), "Age must be in between " + sportType.getMinCoachAge() + " y.o. & " + sportType.getMaxCoachAge() + " y.o. for this sport.");
                }
            }
        } else {
            errors.put("age", "Input age, please.");
        }*/
    }

    public void validateExperience(CoachCreateDto dto, ErrorMap errors) {
        /*int fullYears = Period.between(dto.getDob(), LocalDate.now()).getYears();
        Map<SportType, Long> sports = dto.getSports();
        for (Map.Entry<SportType, Long> entry: sports.entrySet()) {
            SportType sportType = entry.getKey();
            Long exp = entry.getValue();
            if (sportType.getThreshold() > exp) {
                errors.put("experience", sportType.name(), "Experience must be at least " + sportType.getThreshold() + " years for this sport.");
            } else if (fullYears - PersonConstants.MIN_EMPLOYMENT_AGE < exp) {
                errors.put("experience", "Please, input valid experience years.");
            }
        }*/
    }
}
