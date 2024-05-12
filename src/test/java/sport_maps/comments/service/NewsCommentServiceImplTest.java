package sport_maps.comments.service;

import jakarta.persistence.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sport_maps.comments.dao.NewsCommentDao;
import sport_maps.comments.domain.NewsComment;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.nef.dao.NewsDao;
import sport_maps.nef.domain.News;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.Role;
import sport_maps.security.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsCommentServiceImplTest {
    @Mock
    private NewsCommentDao coachDao;

    @Mock
    private UserDao userDao;

    @Mock
    private NewsDao forumDao;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private NewsCommentServiceImpl coachService;

    private NewsComment coach;
    private News forum;
    private CommentSaveDto coachSaveDto;
    private User user;

    @BeforeEach
    void setUp() {
        coachService.setDao(coachDao);
        coachService.setMapper(mapper);
        user = new User();
        user.setEmail("galaxy23@gmail.com");
        user.setEnabled(true);
        user.setRole(Role.ADMIN);
        user.setFirstName("Ivan");
        user.setLastName("Mihai");
        user.setPassword("12qw34erQ++");
        forum = new News();
        forum.setAuthor(user);
        forum.setName("Football Euro 2024");
        forum.setText("32 best european teams will clash during this event.");
        coach = new NewsComment();
        coach.setNews(forum);
        coach.setAuthor(user);
        coach.setText("Best day");
        coachSaveDto = new CommentSaveDto("Football Euro 2024", "galaxy23@gmail.com", 1L);
    }

    @Test
    void create_returnsObject() {
        lenient().when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        lenient().when(forumDao.findById(1L)).thenReturn(Optional.of(forum));
        lenient().when(coachDao.save(Mockito.any(NewsComment.class))).thenReturn(coach);
        coachService.createComment(coachSaveDto);
        Mockito.verify(coachDao, times(1)).save(mapper.convertToEntity(coachSaveDto, user, forum));
    }

    @Test
    void create_fails() {
        CommentSaveDto coachSaveDto1 = new CommentSaveDto("", "galaxy23@gmail.com", 1L);
        CommentSaveDto coachSaveDto2 = new CommentSaveDto("Football Euro 2024", "", 1L);
        CommentSaveDto coachSaveDto3 = new CommentSaveDto("Football Euro 2024", "32 best european teams will clash during this event.", 0L);
        Assertions.assertThatThrownBy(() -> coachService.createComment(coachSaveDto1)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createComment(coachSaveDto2)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createComment(coachSaveDto3)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void update() {
        lenient().when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        lenient().when(forumDao.findById(1L)).thenReturn(Optional.of(forum));
        lenient().when(coachDao.save(Mockito.any(NewsComment.class))).thenReturn(coach);
        lenient().when(mapper.convertToEntity(Mockito.any(CommentSaveDto.class), Mockito.any(User.class), Mockito.any(News.class))).thenReturn(coach);
        coachService.createComment(coachSaveDto);
        CommentSaveDto coachSaveDto2 = new CommentSaveDto("Euro 2024", "galaxy23@gmail.com", 1L);
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(coach));
        lenient().when(mapper.convertToEntity(Mockito.any(CommentSaveDto.class), Mockito.any(User.class), Mockito.any(News.class))).thenReturn(coach);
        lenient().when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        coachService.updateComment(1L, coachSaveDto2);
        verify(coachDao, times(1)).save(mapper.convertToEntity(coachSaveDto2, user, forum));
    }

    @Test
    void deleteById() {
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.of(coach));
        assertAll(() -> coachService.deleteById(1L));
    }
}