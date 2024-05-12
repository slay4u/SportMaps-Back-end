package sport_maps.image.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sport_maps.commons.domain.SportType;
import sport_maps.image.domain.EventImage;
import sport_maps.nef.dao.EventDao;
import sport_maps.nef.domain.Event;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.Role;
import sport_maps.security.domain.User;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EventImageDaoTest {
    @Autowired
    private EventImageDao eventDao;

    @Autowired
    private EventDao userDao;

    @Autowired
    private UserDao userDao1;

    private Event user;
    private EventImage event;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setEmail("galaxy23@gmail.com");
        user1.setEnabled(true);
        user1.setRole(Role.ADMIN);
        user1.setFirstName("Ivan");
        user1.setLastName("Mihai");
        user1.setPassword("12qw34erQ++");
        User savedUser1 = userDao1.save(user1);
        user = new Event();
        user.setAuthor(savedUser1);
        user.setName("Football Euro 2024");
        user.setText("32 best european teams will clash during this event.");
        user.setSportType(SportType.SportKite);
        Event savedUser = userDao.save(user);
        event = new EventImage();
        event.setType("png");
        event.setName("Football Euro 2024");
        event.setFilePath("32 best european teams will clash during this event.");
        event.setEvent(savedUser);
    }

    @Test
    public void saveAll_returnsSavedObject() {
        EventImage savedEvent = eventDao.save(event);
        Assertions.assertThat(savedEvent).isNotNull();
        Assertions.assertThat(savedEvent.getId()).isGreaterThan(0L);
    }

    @Test
    public void getAll_returnsAllObjects() {
        EventImage comment2 = new EventImage();
        comment2.setType("png");
        comment2.setName("Bum 2024");
        comment2.setFilePath("32 best european teams will clash during this event.");
        comment2.setEvent(user);
        eventDao.save(event);
        eventDao.save(comment2);
        List<EventImage> eventComments = eventDao.findAll();
        Assertions.assertThat(eventComments).isNotNull();
        Assertions.assertThat(eventComments.size()).isEqualTo(2);
    }

    @Test
    public void getById_returnsObject() {
        eventDao.save(event);
        EventImage comment1 = eventDao.findById(event.getId()).get();
        Assertions.assertThat(comment1).isNotNull();
    }

    @Test
    public void update_savesObject() {
        eventDao.save(event);
        EventImage comment1 = eventDao.findById(event.getId()).get();
        comment1.setName("Something");
        EventImage updated = eventDao.save(comment1);
        Assertions.assertThat(updated.getName()).isNotNull();
    }

    @Test
    public void delete_deletesObject() {
        eventDao.save(event);
        eventDao.deleteById(event.getId());
        Optional<EventImage> optional = eventDao.findById(event.getId());
        Assertions.assertThat(optional).isEmpty();
    }
}
