package sport_maps.comments.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sport_maps.comments.domain.EventComment;
import sport_maps.commons.domain.SportType;
import sport_maps.nef.dao.EventDao;
import sport_maps.nef.domain.Event;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.Role;
import sport_maps.security.domain.User;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EventCommentDaoTest {
    @Autowired
    private EventCommentDao eventCommentDao;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private UserDao userDao;

    private User user;
    private Event event;
    private EventComment comment;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("galaxy23@gmail.com");
        user.setEnabled(true);
        user.setRole(Role.ADMIN);
        user.setFirstName("Ivan");
        user.setLastName("Mihai");
        user.setPassword("12qw34erQ++");
        User savedUser = userDao.save(user);
        event = new Event();
        event.setSportType(SportType.SportKite);
        event.setAuthor(savedUser);
        event.setName("Football Euro 2024");
        event.setText("32 best european teams will clash during this event.");
        Event savedEvent = eventDao.save(event);
        comment = new EventComment();
        comment.setText("Good job UA POG");
        comment.setEvent(savedEvent);
        comment.setAuthor(savedUser);
    }

    @Test
    public void saveAll_returnsSavedObject() {
        EventComment savedComment = eventCommentDao.save(comment);
        Assertions.assertThat(savedComment).isNotNull();
        Assertions.assertThat(savedComment.getId()).isGreaterThan(0L);
    }

    @Test
    public void getAll_returnsAllObjects() {
        EventComment comment2 = new EventComment();
        comment2.setText("Just good");
        comment2.setEvent(event);
        comment2.setAuthor(user);
        eventCommentDao.save(comment);
        eventCommentDao.save(comment2);
        List<EventComment> eventComments = eventCommentDao.findAll();
        Assertions.assertThat(eventComments).isNotNull();
        Assertions.assertThat(eventComments.size()).isEqualTo(2);
    }

    @Test
    public void getById_returnsObject() {
        eventCommentDao.save(comment);
        EventComment comment1 = eventCommentDao.findById(comment.getId()).get();
        Assertions.assertThat(comment1).isNotNull();
    }

    @Test
    public void update_savesObject() {
        eventCommentDao.save(comment);
        EventComment comment1 = eventCommentDao.findById(comment.getId()).get();
        comment1.setText("Really bad!");
        EventComment updated = eventCommentDao.save(comment1);
        Assertions.assertThat(updated.getText()).isNotNull();
    }

    @Test
    public void delete_deletesObject() {
        eventCommentDao.save(comment);
        eventCommentDao.deleteById(comment.getId());
        Optional<EventComment> optional = eventCommentDao.findById(comment.getId());
        Assertions.assertThat(optional).isEmpty();
    }
}
