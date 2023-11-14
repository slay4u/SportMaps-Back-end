package sport_maps.coach.dao;

import sport_maps.coach.domain.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sport_maps.commons.domain.SportType;

import java.util.Optional;

@Repository
public interface CoachDao extends JpaRepository<Coach, Long> {
    @Query(value = "SELECT COUNT(*) FROM public.coach",
            nativeQuery = true)
    long getAllCoachCount();

    Optional<Coach> findCoachByFirstNameAndLastNameAndAgeAndExperienceAndPriceAndSportType(
            String firstName, String lastName, Long age, Long experience, Double price, SportType sportType);
}
