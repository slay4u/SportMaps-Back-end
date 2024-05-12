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
import sport_maps.commons.domain.SportType;
import sport_maps.commons.util.mapper.Mapper;
import sport_maps.nef.dao.EventDao;
import sport_maps.nef.domain.Event;
import sport_maps.nef.dto.EventDto;
import sport_maps.nef.dto.EventSaveDto;
import sport_maps.security.dao.UserDao;
import sport_maps.security.domain.Role;
import sport_maps.security.domain.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {
    @Mock
    private EventDao coachDao;

    @Mock
    private UserDao userDao;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private EventServiceImpl coachService;

    private Event coach;
    private EventSaveDto coachSaveDto;
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
        coach = new Event();
        coach.setSportType(SportType.SportKite);
        coach.setAuthor(user);
        coach.setName("Football Euro 2024");
        coach.setText("32 best european teams will clash during this event.");
        coachSaveDto = new EventSaveDto("Football Euro 2024", "32 best european teams will clash during this event.",
                "galaxy23@gmail.com", String.valueOf(SportType.SportKite));
    }

    @Test
    void create_returnsObject() {
        lenient().when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        lenient().when(coachDao.save(Mockito.any(Event.class))).thenReturn(coach);
        coachService.createEvent(coachSaveDto);
        Mockito.verify(coachDao).save(mapper.convertToEntity(coachSaveDto, SportType.SportKite, user));
    }

    @Test
    void create_fails() {
        EventSaveDto coachSaveDto1 = new EventSaveDto("", "32 best european teams will clash during this event.",
                "galaxy23@gmail.com", String.valueOf(SportType.SportKite));
        EventSaveDto coachSaveDto2 = new EventSaveDto("Football Euro 2024", "",
                "galaxy23@gmail.com", String.valueOf(SportType.SportKite));
        EventSaveDto coachSaveDto3 = new EventSaveDto("Football Euro 2024", "32 best european teams will clash during this event.",
                "", String.valueOf(SportType.SportKite));
        EventSaveDto coachSaveDto4 = new EventSaveDto("Football Euro 2024", "32 best european teams will clash during this event.",
                "galaxy23@gmail.com", "");
        Assertions.assertThatThrownBy(() -> coachService.createEvent(coachSaveDto1)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createEvent(coachSaveDto2)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createEvent(coachSaveDto3)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createEvent(coachSaveDto4)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update() {
        lenient().when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        lenient().when(coachDao.save(Mockito.any(Event.class))).thenReturn(coach);
        lenient().when(mapper.convertToEntity(Mockito.any(EventSaveDto.class), Mockito.any(SportType.class), Mockito.any(User.class))).thenReturn(coach);
        coachService.createEvent(coachSaveDto);
        EventSaveDto coachSaveDto2 = new EventSaveDto("Euro 2024", "32 best european teams will clash.",
                "galaxy23@gmail.com", String.valueOf(SportType.SportKite));
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(coach));
        lenient().when(mapper.convertToEntity(Mockito.any(EventSaveDto.class), Mockito.any(SportType.class), Mockito.any(User.class))).thenReturn(coach);
        coachService.updateEvent(1L, coachSaveDto2);
        verify(coachDao, times(1)).save(mapper.convertToEntity(coachSaveDto2, SportType.SportKite, user));
    }

    @Test
    void getById() {
        EventDto eventDto = new EventDto(1L, "Football Euro 2024", "fg", "32 best european teams will clash during this event.",
                "galaxy23@gmail.com", String.valueOf(SportType.SportKite), null, null);
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(coach));
        lenient().when(mapper.convertToDto(Mockito.any(Event.class))).thenReturn(eventDto);
        EventDto coachDto = coachService.getEventById(1L);
        Assertions.assertThat(coachDto).isNotNull();
    }

    @Test
    void deleteById() {
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.of(coach));
        assertAll(() -> coachService.deleteById(1L));
    }

    @Test
    void getAll() {
        Page<Event> coaches = Mockito.mock(Page.class);
        when(coachDao.findAll(Mockito.any(Pageable.class))).thenReturn(coaches);
        Page<Event> coaches1 = coachService.getAll(0, 15);
        Assertions.assertThat(coaches1).isNotNull();
    }
}
