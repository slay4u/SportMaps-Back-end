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
import sport_maps.nef.dao.NewsDao;
import sport_maps.nef.domain.News;
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
class NewsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private NewsDao newsDao;

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
        newsDao.deleteAll();
        userDao.deleteAll();
    }

    @Test
    @Order(1)
    void create() throws Exception {
        String newsJSON = """
                {
                "name": "Just News",
                "text": "A lot of words",
                "author": "test@gmail.com"
                }""";
        MockMultipartFile json = new MockMultipartFile("news",  "coachJSON", "application/json", newsJSON.getBytes());
        MockMultipartFile image = new MockMultipartFile("image",  "sm.png", "image/png",
                Files.readAllBytes(Path.of("src/test/resources/images/sm.png")));
        mockMvc.perform(multipart("/sm/news").file(json).file(image).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isCreated()).andExpect(jsonPath("$").doesNotExist());
        News goods = newsDao.findByName("Just News").orElseThrow();
        assertThat(goods.getId()).isGreaterThan(0L);
        assertEquals("Just News", goods.getName());
        assertEquals("A lot of words", goods.getText());
    }

    @Test
    @Order(2)
    void getById() throws Exception {
        MockHttpServletRequestBuilder mockRequest = get("/sm/news/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Just News"))
                .andExpect(jsonPath("$.text").value("A lot of words"));
    }

    @Test
    @Order(3)
    void getAll() throws Exception {
        String newsJSON = """
                {
                "name": "Just News1",
                "text": "A lot of words...",
                "author": "test@gmail.com"
                }""";
        MockMultipartFile json = new MockMultipartFile("news",  "coachJSON", "application/json", newsJSON.getBytes());
        MockMultipartFile image = new MockMultipartFile("image",  "sm.png", "image/png",
                Files.readAllBytes(Path.of("src/test/resources/images/sm.png")));
        mockMvc.perform(multipart("/sm/news").file(json).file(image).header("Authorization", "Bearer " + jwt))
                .andExpect(status().isCreated()).andExpect(jsonPath("$").doesNotExist());
        MockHttpServletRequestBuilder mockRequest = get("/sm/news")
                .param("page", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Just News"))
                .andExpect(jsonPath("$.content[1].name").value("Just News1"))
                .andExpect(jsonPath("$.numberOfElements").value(2));
    }

    @Test
    @Order(4)
    void delete_success() throws Exception {
        MockHttpServletRequestBuilder mockRequest = delete("/sm/news/{id}", 1)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isNoContent()).andExpect(jsonPath("$").doesNotExist());
        MockHttpServletRequestBuilder mockRequestGetAll = get("/sm/news")
                .param("page", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequestGetAll).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Just News1"))
                .andExpect(jsonPath("$.numberOfElements").value(1));
    }

    @Test
    @Order(5)
    void update() throws Exception {
        String newsJSON = """
                {
                "name": "Updated News",
                "text": "Not a lot of words...",
                "author": "test@gmail.com"
                }""";
        MockMultipartFile json = new MockMultipartFile("news",  "coachJSON", "application/json", newsJSON.getBytes());
        MockMultipartFile image = new MockMultipartFile("image",  "sm.png", "image/png",
                Files.readAllBytes(Path.of("src/test/resources/images/sm.png")));
        mockMvc.perform(multipart(HttpMethod.PUT, "/sm/news/{id}", 2L).file(json).file(image)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isAccepted()).andExpect(jsonPath("$").doesNotExist());
        News goods = newsDao.findByName("Updated News").orElseThrow();
        assertThat(goods.getId()).isEqualTo(2L);
        assertEquals("Updated News", goods.getName());
        assertEquals("Not a lot of words...", goods.getText());
    }
}
