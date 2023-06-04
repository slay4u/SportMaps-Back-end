package spring.app.modules.coach.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import spring.app.modules.coach.domain.Coach;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoachDao extends JpaRepository<Coach, Long> {
    @Query(value = "SELECT * FROM public.coaches", nativeQuery = true)
    List<Coach> getAllCoaches(Pageable pageable);

    @Query(value = "SELECT * FROM public.coaches" +
            " WHERE id_coach = ?1", nativeQuery = true)
    Optional<Coach> getCoachById(Long id);

    @Query(value = "SELECT COUNT(*) FROM public.coaches",
            nativeQuery = true)
    long getAllCoachCount();

    @Query(value = "SELECT * FROM public.coaches" +
            " WHERE first_name = ?1 AND last_name = ?2" +
            " AND age = ?3 AND experience = ?4" +
            " AND price = ?5 AND sport_type = ?6", nativeQuery = true)
    Optional<Coach> getCoachByAllFields(String firstName,
                                        String lastName,
                                        Long age,
                                        Long experience,
                                        Double price,
                                        String sportType);
}
