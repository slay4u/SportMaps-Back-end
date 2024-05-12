package sport_maps.coach.dao;

import sport_maps.coach.domain.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sport_maps.commons.domain.SportType;

import java.util.Optional;

@Repository
public interface CoachDao extends JpaRepository<Coach, Long> {
    boolean existsByFirstNameAndLastNameAndAgeAndExperienceAndPriceAndSportType(
            String firstName, String lastName, Long age, Long experience, Double price, SportType sportType);

    Optional<Coach> findByFirstNameAndLastName(String firstName, String lastName);
}
