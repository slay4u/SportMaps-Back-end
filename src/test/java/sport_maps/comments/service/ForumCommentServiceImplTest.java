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
import sport_maps.comments.dao.ForumCommentDao;
import sport_maps.comments.domain.ForumComment;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.nef.dao.ForumDao;
import sport_maps.nef.domain.Forum;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.Role;
import sport_maps.security.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumCommentServiceImplTest {
    @Mock
    private ForumCommentDao coachDao;

    @Mock
    private UserDao userDao;

    @Mock
    private ForumDao forumDao;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private ForumCommentServiceImpl coachService;

    private ForumComment coach;
    private Forum forum;
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
        forum = new Forum();
        forum.setAuthor(user);
        forum.setName("Football Euro 2024");
        forum.setText("32 best european teams will clash during this event.");
        coach = new ForumComment();
        coach.setForum(forum);
        coach.setAuthor(user);
        coach.setText("Best day");
        coachSaveDto = new CommentSaveDto("Football Euro 2024", "galaxy23@gmail.com", 1L);
    }

    @Test
    void create_returnsObject() {
        lenient().when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        lenient().when(forumDao.findById(1L)).thenReturn(Optional.of(forum));
        lenient().when(coachDao.save(Mockito.any(ForumComment.class))).thenReturn(coach);
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
        lenient().when(forumDao.findById(Mockito.any(Long.class))).thenReturn(Optional.of(forum));
        lenient().when(coachDao.save(Mockito.any(ForumComment.class))).thenReturn(coach);
        lenient().when(mapper.convertToEntity(Mockito.any(CommentSaveDto.class), Mockito.any(User.class), Mockito.any(Forum.class))).thenReturn(coach);
        coachService.createComment(coachSaveDto);
        CommentSaveDto coachSaveDto2 = new CommentSaveDto("Euro 2024", "galaxy23@gmail.com", 1L);
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.of(coach));
        lenient().when(forumDao.findById(Mockito.any(Long.class))).thenReturn(Optional.of(forum));
        lenient().when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        lenient().when(mapper.convertToEntity(Mockito.any(CommentSaveDto.class), Mockito.any(User.class), Mockito.any(Forum.class))).thenReturn(coach);
        coachService.updateComment(1L, coachSaveDto2);
        verify(coachDao, times(1)).save(mapper.convertToEntity(coachSaveDto2, user, forum));
    }

    @Test
    void deleteById() {
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.of(coach));
        assertAll(() -> coachService.deleteById(1L));
    }
}
