package sport_maps.comments.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import sport_maps.comments.domain.NewsComment;
import sport_maps.nef.dao.NewsDao;
import sport_maps.nef.domain.News;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.Role;
import sport_maps.security.domain.User;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class NewsCommentDaoTest {
    @Autowired
    private NewsCommentDao newsCommentDao;

    @Autowired
    private NewsDao newsDao;

    @Autowired
    private UserDao userDao;

    private User user;
    private News news;
    private NewsComment comment;

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
        news = new News();
        news.setAuthor(savedUser);
        news.setName("Football Euro 2024");
        news.setText("32 best european teams will clash during this event.");
        News savedNews = newsDao.save(news);
        comment = new NewsComment();
        comment.setText("Good job UA POG");
        comment.setNews(savedNews);
        comment.setAuthor(savedUser);
    }

    @Test
    public void saveAll_returnsSavedObject() {
        NewsComment savedComment = newsCommentDao.save(comment);
        Assertions.assertThat(savedComment).isNotNull();
        Assertions.assertThat(savedComment.getId()).isGreaterThan(0L);
    }

    @Test
    public void getAll_returnsAllObjects() {
        NewsComment comment2 = new NewsComment();
        comment2.setText("Just good");
        comment2.setNews(news);
        comment2.setAuthor(user);
        newsCommentDao.save(comment);
        newsCommentDao.save(comment2);
        List<NewsComment> eventComments = newsCommentDao.findAll();
        Assertions.assertThat(eventComments).isNotNull();
        Assertions.assertThat(eventComments.size()).isEqualTo(2);
    }

    @Test
    public void getById_returnsObject() {
        newsCommentDao.save(comment);
        NewsComment comment1 = newsCommentDao.findById(comment.getId()).get();
        Assertions.assertThat(comment1).isNotNull();
    }

    @Test
    public void update_savesObject() {
        newsCommentDao.save(comment);
        NewsComment comment1 = newsCommentDao.findById(comment.getId()).get();
        comment1.setText("Really bad!");
        NewsComment updated = newsCommentDao.save(comment1);
        Assertions.assertThat(updated.getText()).isNotNull();
    }

    @Test
    public void delete_deletesObject() {
        newsCommentDao.save(comment);
        newsCommentDao.deleteById(comment.getId());
        Optional<NewsComment> optional = newsCommentDao.findById(comment.getId());
        Assertions.assertThat(optional).isEmpty();
    }
}
