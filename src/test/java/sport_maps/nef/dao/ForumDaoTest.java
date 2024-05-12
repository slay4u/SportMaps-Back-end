package sport_maps.nef.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sport_maps.nef.domain.Forum;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.Role;
import sport_maps.security.domain.User;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ForumDaoTest {
    @Autowired
    private ForumDao eventDao;

    @Autowired
    private UserDao userDao;

    private User user;
    private Forum event;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("galaxy23@gmail.com");
        user.setEnabled(true);
        user.setRole(Role.ADMIN);
        user.setFirstName("Ivan");
        user.setLastName("Mihai");
        user.setPassword("12qw34erQ++");
        User savedUser = userDao.save(user);
        event = new Forum();
        event.setAuthor(savedUser);
        event.setName("Football Euro 2024");
        event.setText("32 best european teams will clash during this event.");
    }

    @Test
    public void saveAll_returnsSavedObject() {
        Forum savedEvent = eventDao.save(event);
        Assertions.assertThat(savedEvent).isNotNull();
        Assertions.assertThat(savedEvent.getId()).isGreaterThan(0L);
    }

    @Test
    public void getAll_returnsAllObjects() {
        Forum comment2 = new Forum();
        comment2.setText("Just good");
        comment2.setName("Bum 2024");
        comment2.setAuthor(user);
        eventDao.save(event);
        eventDao.save(comment2);
        List<Forum> eventComments = eventDao.findAll();
        Assertions.assertThat(eventComments).isNotNull();
        Assertions.assertThat(eventComments.size()).isEqualTo(2);
    }

    @Test
    public void getById_returnsObject() {
        eventDao.save(event);
        Forum comment1 = eventDao.findById(event.getId()).get();
        Assertions.assertThat(comment1).isNotNull();
    }

    @Test
    public void update_savesObject() {
        eventDao.save(event);
        Forum comment1 = eventDao.findById(event.getId()).get();
        comment1.setText("Really bad!");
        Forum updated = eventDao.save(comment1);
        Assertions.assertThat(updated.getText()).isNotNull();
    }

    @Test
    public void delete_deletesObject() {
        eventDao.save(event);
        eventDao.deleteById(event.getId());
        Optional<Forum> optional = eventDao.findById(event.getId());
        Assertions.assertThat(optional).isEmpty();
    }
}
