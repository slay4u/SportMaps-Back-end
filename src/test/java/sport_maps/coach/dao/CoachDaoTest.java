package sport_maps.coach.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sport_maps.coach.domain.Coach;
import sport_maps.commons.domain.SportType;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CoachDaoTest {
    @Autowired
    private CoachDao coachDao;

    private Coach coach;

    @BeforeEach
    public void setUp() {
        coach = new Coach();
        coach.setDescription("A lot of exp, good at basketball");
        coach.setAge(23L);
        coach.setFirstName("Ivan");
        coach.setLastName("Mihai");
        coach.setExperience(10L);
        coach.setPrice(100.5);
        coach.setSportType(SportType.SportKite);
    }

    @Test
    public void saveAll_returnsSavedObject() {
        Coach savedCoach = coachDao.save(coach);
        Assertions.assertThat(savedCoach).isNotNull();
        Assertions.assertThat(savedCoach.getId()).isGreaterThan(0L);
    }

    @Test
    public void getAll_returnsAllObjects() {
        Coach coach2 = new Coach();
        coach2.setDescription("Bad at everything");
        coach2.setAge(20L);
        coach2.setFirstName("Ivan");
        coach2.setLastName("Mihai");
        coach2.setExperience(15L);
        coach2.setPrice(200.5);
        coach2.setSportType(SportType.SportKite);
        coachDao.save(coach);
        coachDao.save(coach2);
        List<Coach> eventComments = coachDao.findAll();
        Assertions.assertThat(eventComments).isNotNull();
        Assertions.assertThat(eventComments.size()).isEqualTo(2);
    }

    @Test
    public void getById_returnsObject() {
        coachDao.save(coach);
        Coach comment1 = coachDao.findById(coach.getId()).get();
        Assertions.assertThat(comment1).isNotNull();
    }

    @Test
    public void update_savesObject() {
        coachDao.save(coach);
        Coach comment1 = coachDao.findById(coach.getId()).get();
        comment1.setDescription("Really bad!");
        Coach updated = coachDao.save(comment1);
        Assertions.assertThat(updated.getDescription()).isNotNull();
    }

    @Test
    public void delete_deletesObject() {
        coachDao.save(coach);
        coachDao.deleteById(coach.getId());
        Optional<Coach> optional = coachDao.findById(coach.getId());
        Assertions.assertThat(optional).isEmpty();
    }
}
