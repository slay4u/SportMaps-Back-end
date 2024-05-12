package sport_maps.coach.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sport_maps.coach.dao.CoachDao;
import sport_maps.coach.domain.Coach;
import sport_maps.coach.dto.CoachDto;
import sport_maps.coach.dto.CoachSaveDto;
import sport_maps.commons.domain.SportType;
import sport_maps.commons.util.mapper.Mapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoachServiceImplTest {
    @Mock
    private CoachDao coachDao;

    @Mock
    private Mapper mapper;

    @InjectMocks
    private CoachServiceImpl coachService;

    private Coach coach;
    private CoachSaveDto coachSaveDto;

    @BeforeEach
    void setUp() {
        coachService.setDao(coachDao);
        coachService.setMapper(mapper);
        coach = new Coach();
        coach.setFirstName("Ivan");
        coach.setLastName("Mihai");
        coach.setAge(23L);
        coach.setExperience(10L);
        coach.setPrice(100.5);
        coach.setDescription("A lot of exp, good at basketball");
        coach.setSportType(SportType.SportKite);
        coachSaveDto = new CoachSaveDto(
                "Ivan", "Mihai", 23L, 10L, 100.5,
                "A lot of exp, good at basketball", String.valueOf(SportType.SportKite)
        );
    }

    @AfterEach
    void tearDown() {
        coachDao.deleteAll();
    }

    @Test
    void create_returnsObject() {
        lenient().when(coachDao.save(Mockito.any(Coach.class))).thenReturn(coach);
        coachService.createCoach(coachSaveDto);
        Mockito.verify(coachDao, times(1)).save(mapper.convertToEntity(coachSaveDto, SportType.SportKite));
    }

    @Test
    void create_fails() {
        CoachSaveDto coachSaveDto1 = new CoachSaveDto(
                "", "Mihai", 23L, 10L, 100.5,
                "A lot of exp, good at basketball", String.valueOf(SportType.SportKite)
        );
        CoachSaveDto coachSaveDto2 = new CoachSaveDto(
                "Ivan", "", 23L, 10L, 100.5,
                "A lot of exp, good at basketball", String.valueOf(SportType.SportKite)
        );
        CoachSaveDto coachSaveDto3 = new CoachSaveDto(
                "Ivan", "Mihai", 0L, 10L, 100.5,
                "A lot of exp, good at basketball", String.valueOf(SportType.SportKite)
        );
        CoachSaveDto coachSaveDto4 = new CoachSaveDto(
                "Ivan", "Mihai", 23L, 0L, 100.5,
                "A lot of exp, good at basketball", String.valueOf(SportType.SportKite)
        );
        CoachSaveDto coachSaveDto5 = new CoachSaveDto(
                "Ivan", "Mihai", 23L, 10L, 0.0,
                "A lot of exp, good at basketball", String.valueOf(SportType.SportKite)
        );
        CoachSaveDto coachSaveDto6 = new CoachSaveDto(
                "Ivan", "Mihai", 23L, 10L, 100.5,
                "", String.valueOf(SportType.SportKite)
        );
        CoachSaveDto coachSaveDto7 = new CoachSaveDto(
                "Ivan", "Mihai", 23L, 10L, 100.5,
                "A lot of exp, good at basketball", ""
        );
        Assertions.assertThatThrownBy(() -> coachService.createCoach(coachSaveDto1)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createCoach(coachSaveDto2)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createCoach(coachSaveDto3)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createCoach(coachSaveDto4)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createCoach(coachSaveDto5)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createCoach(coachSaveDto6)).isInstanceOf(IllegalArgumentException.class);
        Assertions.assertThatThrownBy(() -> coachService.createCoach(coachSaveDto7)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void update() {
        lenient().when(mapper.convertToEntity(Mockito.any(CoachSaveDto.class), Mockito.any(SportType.class))).thenReturn(coach);
        lenient().when(coachDao.save(Mockito.any(Coach.class))).thenReturn(coach);
        coachService.createCoach(coachSaveDto);
        CoachSaveDto coachSaveDto2 = new CoachSaveDto(
                "Fal", "Mih", 20L, 6L, 10.5,
                "A lot of exp, good at basketball", String.valueOf(SportType.SportKite)
        );
        lenient().when(mapper.convertToEntity(Mockito.any(CoachSaveDto.class), Mockito.any(SportType.class))).thenReturn(coach);
        coachService.updateCoach(1L, coachSaveDto2);
        verify(coachDao).save(mapper.convertToEntity(coachSaveDto2, SportType.SportKite));
    }

    @Test
    void getById() {
        CoachDto coachDto1 = new CoachDto(1L, "Fal", "Mih", 20L, 6L, 10.5,
                "A lot of exp, good at basketball", String.valueOf(SportType.SportKite), null);
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.ofNullable(coach));
        lenient().when(mapper.convertToDto(Mockito.any(Coach.class))).thenReturn(coachDto1);
        CoachDto coachDto = coachService.getCoachById(1L);
        Assertions.assertThat(coachDto).isNotNull();
    }

    @Test
    void deleteById() {
        lenient().when(coachDao.findById(Mockito.any(Long.class))).thenReturn(Optional.of(coach));
        assertAll(() -> coachService.deleteById(1L));
    }

    @Test
    void getAll() {
        Page<Coach> coaches = Mockito.mock(Page.class);
        when(coachDao.findAll(Mockito.any(Pageable.class))).thenReturn(coaches);
        Page<Coach> coaches1 = coachService.getAll(0, 15);
        Assertions.assertThat(coaches1).isNotNull();
    }
}
