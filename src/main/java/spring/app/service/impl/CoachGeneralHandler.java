package spring.app.service.impl;

import spring.app.domain.Coach;
import spring.app.dto.coach.CoachAllInfoDto;

import java.util.List;
import java.util.stream.Collectors;

public interface CoachGeneralHandler {
    default CoachAllInfoDto allInfoDto(Coach coach) {
        return CoachAllInfoDto.builder()
                .id(coach.getIdCoach())
                .firstName(coach.getFirstName())
                .lastName(coach.getLastName())
                .age(coach.getAge())
                .experience(coach.getExperience())
                .price(coach.getPrice())
                .description(coach.getDescription())
                .sportType(String.valueOf(coach.getSportType()))
                .build();
    }

    default List<CoachAllInfoDto> listToDto(List<Coach> coaches) {
        return coaches.stream().map(this::allInfoDto).collect(Collectors.toList());
    }
}
