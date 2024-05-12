package sport_maps.coach.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import sport_maps.coach.dao.CoachDao;
import sport_maps.coach.domain.Coach;
import sport_maps.security.dao.UserDao;
import sport_maps.security.dao.VerificationTokenDao;
import sport_maps.security.domain.VerificationToken;
import sport_maps.security.dto.LoginRequest;
import sport_maps.security.dto.RegisterRequest;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CoachControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CoachDao coachDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private VerificationTokenDao verificationTokenDao;

    private String jwt;

    @BeforeAll
    void init() throws Exception {
        RegisterRequest signUpDto = new RegisterRequest("Test", "User", "test@gmail.com", "Test12345^");
        LoginRequest signInDto = new LoginRequest("test@gmail.com", "Test12345^");
        MockHttpServletRequestBuilder mockRequestSignup = post("/sm/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signUpDto));
        mockMvc.perform(mockRequestSignup).andExpect(status().isCreated());
        VerificationToken verificationToken = verificationTokenDao.findAll().getFirst();
        MockHttpServletRequestBuilder mockRequest = get("/sm/auth/" + verificationToken.getToken())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isAccepted());
        MockHttpServletRequestBuilder mockRequestLogin = post("/sm/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signInDto));
        MvcResult loginResult = mockMvc.perform(mockRequestLogin).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty()).andReturn();
        this.jwt = loginResult.getResponse().getContentAsString();
    }

    @AfterAll
    void tearDown() {
        userDao.deleteAll();
        coachDao.deleteAll();
    }

    @Test
    @Order(1)
    void create() throws Exception {
        String coachJSON = """
                {
                "firstName": "Ivan",
                "lastName": "Mihai",
                "age": 23,
                "experience": 10,
                "price": 100.5,
                "description": "A lot of exp, good at basketball",
                "sportType": "SportKite"
                }""";
        MockMultipartFile json = new MockMultipartFile("coach",  "coachJSON", "application/json", coachJSON.getBytes());
        MockMultipartFile image = new MockMultipartFile("image",  "sm.png", "image/png",
                Files.readAllBytes(Path.of("src/test/resources/images/sm.png")));
        mockMvc.perform(multipart("/sm/coaches").file(json).file(image).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isCreated()).andExpect(jsonPath("$").doesNotExist());
        Coach coach1 = coachDao.findByFirstNameAndLastName("Ivan", "Mihai").orElseThrow();
        assertThat(coach1.getId()).isGreaterThan(0L);
        assertEquals(100.5, coach1.getPrice());
        assertEquals("A lot of exp, good at basketball", coach1.getDescription());
        assertEquals(23, coach1.getAge());
        assertEquals(10, coach1.getExperience());
    }

    @Test
    @Order(2)
    void getById() throws Exception {
        MockHttpServletRequestBuilder mockRequest = get("/sm/coaches/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.price").value(100.5))
                .andExpect(jsonPath("$.description").value("A lot of exp, good at basketball"))
                .andExpect(jsonPath("$.age").value(23L))
                .andExpect(jsonPath("$.experience").value(10L));
    }

    @Test
    @Order(3)
    void getAll() throws Exception {
        String coachJSON = """
                {
                "firstName": "Ivane",
                "lastName": "MihaiL",
                "age": 20,
                "experience": 8,
                "price": 100.5,
                "description": "A lot of exp",
                "sportType": "SportKite"
                }""";
        MockMultipartFile json = new MockMultipartFile("coach",  "coachJSON", "application/json", coachJSON.getBytes());
        MockMultipartFile image = new MockMultipartFile("image",  "sm.png", "image/png",
                Files.readAllBytes(Path.of("src/test/resources/images/sm.png")));
        mockMvc.perform(multipart("/sm/coaches").file(json).file(image).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isCreated()).andExpect(jsonPath("$").doesNotExist());
        MockHttpServletRequestBuilder mockRequest = get("/sm/coaches")
                .param("page", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[1].id").value(2L))
                .andExpect(jsonPath("$.numberOfElements").value(2));
    }

    @Test
    @Order(4)
    void delete_success() throws Exception {
        MockHttpServletRequestBuilder mockRequest = delete("/sm/coaches/{id}", 1L)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isNoContent()).andExpect(jsonPath("$").doesNotExist());
        MockHttpServletRequestBuilder mockRequestGetAll = get("/sm/coaches")
                .param("page", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequestGetAll).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].description").value("A lot of exp"))
                .andExpect(jsonPath("$.numberOfElements").value(1));
    }

    @Test
    @Order(5)
    void update() throws Exception {
        String coachJSON = """
                {
                "firstName": "Bad",
                "lastName": "Guy",
                "age": 34,
                "experience": 12,
                "price": 95.5,
                "description": "Not a lot of exp",
                "sportType": "SportKite"
                }""";
        MockMultipartFile json = new MockMultipartFile("coach",  "coachJSON", "application/json", coachJSON.getBytes());
        MockMultipartFile image = new MockMultipartFile("image",  "sm.png", "image/png",
                Files.readAllBytes(Path.of("src/test/resources/images/sm.png")));
        mockMvc.perform(multipart(HttpMethod.PUT, "/sm/coaches/{id}", 2L).file(json).file(image)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isAccepted()).andExpect(jsonPath("$").doesNotExist());
        Coach goods = coachDao.findByFirstNameAndLastName("Bad", "Guy").orElseThrow();
        assertThat(goods.getId()).isEqualTo(2L);
        assertEquals("Bad", goods.getFirstName());
        assertEquals("Guy", goods.getLastName());
        assertEquals(95.5, goods.getPrice());
        assertEquals("Not a lot of exp", goods.getDescription());
        assertEquals(34, goods.getAge());
        assertEquals(12, goods.getExperience());
    }
}
