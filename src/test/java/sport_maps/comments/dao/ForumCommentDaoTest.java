package sport_maps.comments.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sport_maps.comments.domain.ForumComment;
import sport_maps.nef.dao.ForumDao;
import sport_maps.nef.domain.Forum;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.Role;
import sport_maps.security.domain.User;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ForumCommentDaoTest {
    @Autowired
    private ForumCommentDao forumCommentDao;

    @Autowired
    private ForumDao forumDao;

    @Autowired
    private UserDao userDao;

    private User user;
    private Forum forum;
    private ForumComment comment;

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
        forum = new Forum();
        forum.setAuthor(savedUser);
        forum.setName("Football Euro 2024");
        forum.setText("32 best european teams will clash during this event.");
        Forum savedForum = forumDao.save(forum);
        comment = new ForumComment();
        comment.setText("Good job UA POG");
        comment.setForum(savedForum);
        comment.setAuthor(savedUser);
    }

    @Test
    public void saveAll_returnsSavedObject() {
        ForumComment savedComment = forumCommentDao.save(comment);
        Assertions.assertThat(savedComment).isNotNull();
        Assertions.assertThat(savedComment.getId()).isGreaterThan(0L);
    }

    @Test
    public void getAll_returnsAllObjects() {
        ForumComment comment2 = new ForumComment();
        comment2.setText("Just good");
        comment2.setForum(forum);
        comment2.setAuthor(user);
        forumCommentDao.save(comment);
        forumCommentDao.save(comment2);
        List<ForumComment> eventComments = forumCommentDao.findAll();
        Assertions.assertThat(eventComments).isNotNull();
        Assertions.assertThat(eventComments.size()).isEqualTo(2);
    }

    @Test
    public void getById_returnsObject() {
        forumCommentDao.save(comment);
        ForumComment comment1 = forumCommentDao.findById(comment.getId()).get();
        Assertions.assertThat(comment1).isNotNull();
    }

    @Test
    public void update_savesObject() {
        forumCommentDao.save(comment);
        ForumComment comment1 = forumCommentDao.findById(comment.getId()).get();
        comment1.setText("Really bad!");
        ForumComment updated = forumCommentDao.save(comment1);
        Assertions.assertThat(updated.getText()).isNotNull();
    }

    @Test
    public void delete_deletesObject() {
        forumCommentDao.save(comment);
        forumCommentDao.deleteById(comment.getId());
        Optional<ForumComment> optional = forumCommentDao.findById(comment.getId());
        Assertions.assertThat(optional).isEmpty();
    }
}
