package sport_maps.image.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sport_maps.coach.dao.CoachDao;
import sport_maps.coach.domain.Coach;
import sport_maps.commons.domain.SportType;
import sport_maps.image.domain.CoachImage;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CoachImageDaoTest {
    @Autowired
    private CoachImageDao eventDao;

    @Autowired
    private CoachDao userDao;

    private Coach user;
    private CoachImage event;

    @BeforeEach
    void setUp() {
        user = new Coach();
        user.setFirstName("Ivan");
        user.setLastName("Mihai");
        user.setAge(23L);
        user.setExperience(10L);
        user.setPrice(100.5);
        user.setDescription("A lot of exp, good at basketball");
        user.setSportType(SportType.SportKite);
        Coach savedUser = userDao.save(user);
        event = new CoachImage();
        event.setType("png");
        event.setName("Football Euro 2024");
        event.setFilePath("32 best european teams will clash during this event.");
        event.setCoach(savedUser);
    }

    @Test
    public void saveAll_returnsSavedObject() {
        CoachImage savedEvent = eventDao.save(event);
        Assertions.assertThat(savedEvent).isNotNull();
        Assertions.assertThat(savedEvent.getId()).isGreaterThan(0L);
    }

    @Test
    public void getAll_returnsAllObjects() {
        CoachImage comment2 = new CoachImage();
        comment2.setType("png");
        comment2.setName("Bum 2024");
        comment2.setFilePath("32 best european teams will clash during this event.");
        comment2.setCoach(user);
        eventDao.save(event);
        eventDao.save(comment2);
        List<CoachImage> eventComments = eventDao.findAll();
        Assertions.assertThat(eventComments).isNotNull();
        Assertions.assertThat(eventComments.size()).isEqualTo(2);
    }

    @Test
    public void getById_returnsObject() {
        eventDao.save(event);
        CoachImage comment1 = eventDao.findById(event.getId()).get();
        Assertions.assertThat(comment1).isNotNull();
    }

    @Test
    public void update_savesObject() {
        eventDao.save(event);
        CoachImage comment1 = eventDao.findById(event.getId()).get();
        comment1.setName("Something");
        CoachImage updated = eventDao.save(comment1);
        Assertions.assertThat(updated.getName()).isNotNull();
    }

    @Test
    public void delete_deletesObject() {
        eventDao.save(event);
        eventDao.deleteById(event.getId());
        Optional<CoachImage> optional = eventDao.findById(event.getId());
        Assertions.assertThat(optional).isEmpty();
    }
}
