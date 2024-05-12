package sport_maps.nef.controller;

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
import sport_maps.nef.dao.EventDao;
import sport_maps.nef.domain.Event;
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
class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private EventDao eventDao;

    @Autowired
    private VerificationTokenDao verificationTokenDao;

    @Autowired
    private UserDao userDao;

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
        eventDao.deleteAll();
        userDao.deleteAll();
    }

    @Test
    @Order(1)
    void create() throws Exception {
        String newsJSON = """
                {
                "name": "Just Event",
                "text": "A lot of words event",
                "author": "test@gmail.com",
                "sportType": "SportKite"
                }""";
        MockMultipartFile json = new MockMultipartFile("event",  "coachJSON", "application/json", newsJSON.getBytes());
        MockMultipartFile image = new MockMultipartFile("image",  "sm.png", "image/png",
                Files.readAllBytes(Path.of("src/test/resources/images/sm.png")));
        mockMvc.perform(multipart("/sm/events").file(json).file(image).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isCreated()).andExpect(jsonPath("$").doesNotExist());
        Event goods = eventDao.findByName("Just Event").orElseThrow();
        assertThat(goods.getId()).isGreaterThan(0L);
        assertEquals("Just Event", goods.getName());
        assertEquals("A lot of words event", goods.getText());
    }

    @Test
    @Order(2)
    void getById() throws Exception {
        MockHttpServletRequestBuilder mockRequest = get("/sm/events/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Just Event"))
                .andExpect(jsonPath("$.text").value("A lot of words event"));
    }

    @Test
    @Order(3)
    void getAll() throws Exception {
        String newsJSON = """
                {
                "name": "Just Event1",
                "text": "A lot of words...",
                "author": "test@gmail.com",
                "sportType": "SportKite"
                }""";
        MockMultipartFile json = new MockMultipartFile("event",  "coachJSON", "application/json", newsJSON.getBytes());
        MockMultipartFile image = new MockMultipartFile("image",  "sm.png", "image/png",
                Files.readAllBytes(Path.of("src/test/resources/images/sm.png")));
        mockMvc.perform(multipart("/sm/events").file(json).file(image).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isCreated()).andExpect(jsonPath("$").doesNotExist());
        MockHttpServletRequestBuilder mockRequest = get("/sm/events")
                .param("page", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Just Event"))
                .andExpect(jsonPath("$.content[1].name").value("Just Event1"))
                .andExpect(jsonPath("$.numberOfElements").value(2));
    }

    @Test
    @Order(4)
    void delete_success() throws Exception {
        MockHttpServletRequestBuilder mockRequest = delete("/sm/events/{id}", 1)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isNoContent()).andExpect(jsonPath("$").doesNotExist());
        MockHttpServletRequestBuilder mockRequestGetAll = get("/sm/events")
                .param("page", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequestGetAll).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Just Event1"))
                .andExpect(jsonPath("$.numberOfElements").value(1));
    }

    @Test
    @Order(5)
    void update() throws Exception {
        String newsJSON = """
                {
                "name": "Updated Event",
                "text": "Not a lot of words... event",
                "author": "test@gmail.com",
                "sportType": "SportKite"
                }""";
        MockMultipartFile json = new MockMultipartFile("event",  "coachJSON", "application/json", newsJSON.getBytes());
        MockMultipartFile image = new MockMultipartFile("image",  "sm.png", "image/png",
                Files.readAllBytes(Path.of("src/test/resources/images/sm.png")));
        mockMvc.perform(multipart(HttpMethod.PUT, "/sm/events/{id}", 2L).file(json).file(image)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isAccepted()).andExpect(jsonPath("$").doesNotExist());
        Event goods = eventDao.findByName("Updated Event").orElseThrow();
        assertThat(goods.getId()).isEqualTo(2L);
        assertEquals("Updated Event", goods.getName());
        assertEquals("Not a lot of words... event", goods.getText());
    }
}
