package sport_maps.nef.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.nef.dao.ForumDao;
import sport_maps.nef.domain.Forum;
import sport_maps.nef.dto.ForumDto;
import sport_maps.nef.dto.ForumSaveDto;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.Role;
import sport_maps.security.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumServiceImplTest {
    @Mock
    private ForumDao coachDao;

    @Mock
    private UserDao userDao;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private ForumServiceImpl coachService;

    private Forum coach;
    private ForumSaveDto coachSaveDto;
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
        coach = new Forum();
        coach.setAuthor(user);
        coach.setName("Football Euro 2024");
        coach.setText("32 best european teams will clash during this event.");
        coachSaveDto = new ForumSaveDto("Football Euro 2024", "32 best european teams will clash during this event.",
                "galaxy23@gmail.com");
    }

    @Test
    void create_returnsObject() {
        lenient().when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        lenient().when(coachDao.save(Mockito.any(Forum.class))).thenReturn(coach);
        coachService.createForum(coachSaveDto);
        Mockito.verify(coachDao, times(1)).save(mapper.convertToEntity(coachSaveDto, user));
    }

    @Test
    void create_fails() {
        ForumSaveDto coachSaveDto1 = new ForumSaveDto("", "32 best european teams will clash during this event.",
                "galaxy23@gmail.com");
        ForumSaveDto coachSaveDto2 = new ForumSaveDto("Football Euro 2024", "",
                "galaxy23@gmail.com");
        ForumSaveDto coachSaveDto3 = new ForumSaveDto("Football Euro 2024", "32 best european teams will clash during this event.",
                "");
        Assertions.assertThatThrownBy(() -> coachService.createForum(coachSaveDto1)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createForum(coachSaveDto2)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createForum(coachSaveDto3)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update() {
        lenient().when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        lenient().when(coachDao.save(Mockito.any(Forum.class))).thenReturn(coach);
        lenient().when(mapper.convertToEntity(Mockito.any(ForumSaveDto.class), Mockito.any(User.class))).thenReturn(coach);
        coachService.createForum(coachSaveDto);
        ForumSaveDto coachSaveDto2 = new ForumSaveDto("Euro 2024", "32 best european teams will clash.",
                "galaxy23@gmail.com");
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(coach));
        lenient().when(mapper.convertToEntity(Mockito.any(ForumSaveDto.class), Mockito.any(User.class))).thenReturn(coach);
        coachService.updateForum(1L, coachSaveDto2);
        verify(coachDao, times(1)).save(mapper.convertToEntity(coachSaveDto2, user));
    }

    @Test
    void getById() {
        ForumDto eventDto = new ForumDto(1L, "Football Euro 2024", "fg", "32 best european teams will clash during this event.",
                "galaxy23@gmail.com", null);
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(coach));
        lenient().when(mapper.convertToDto(Mockito.any(Forum.class))).thenReturn(eventDto);
        ForumDto coachDto = coachService.getForumById(1L);
        Assertions.assertThat(coachDto).isNotNull();
    }

    @Test
    void deleteById() {
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.of(coach));
        assertAll(() -> coachService.deleteById(1L));
    }

    @Test
    void getAll() {
        Page<Forum> coaches = Mockito.mock(Page.class);
        when(coachDao.findAll(Mockito.any(Pageable.class))).thenReturn(coaches);
        Page<Forum> coaches1 = coachService.getAll(0, 15);
        Assertions.assertThat(coaches1).isNotNull();
    }
}
