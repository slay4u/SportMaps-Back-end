package spring.app.modules.commons.validation;

import spring.app.modules.commons.constant.SystemConstants;
import spring.app.modules.commons.other.ErrorMap;
import spring.app.modules.commons.other.PersonDto;

import java.time.LocalDate;
import java.time.Period;

public abstract class PersonDtoValidator<DTO extends PersonDto> {

    public ErrorMap validate(DTO e, ErrorMap errors) {
        validateAge(e, errors);
        return errors;
    }

    public void validateAge(DTO e, ErrorMap errors) {
        if (e.getDob() != null) {
            int fullYears = Period.between(e.getDob(), LocalDate.now()).getYears();
            if (fullYears < SystemConstants.MIN_AGE || fullYears > SystemConstants.MAX_AGE) {
                errors.put("age", "Age must be in between " + SystemConstants.MIN_AGE + " y.o. & " + SystemConstants.MAX_AGE + " y.o.");
            }
        } else {
            errors.put("age", "Input age, please.");
        }
    }
}
