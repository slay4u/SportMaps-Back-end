package sport_maps.nef.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import sport_maps.nef.dao.ForumDao;
import sport_maps.nef.domain.Forum;
import sport_maps.nef.dto.ForumSaveDto;
import sport_maps.security.dao.UserDao;
import sport_maps.security.dao.VerificationTokenDao;
import sport_maps.security.domain.VerificationToken;
import sport_maps.security.dto.LoginRequest;
import sport_maps.security.dto.RegisterRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ForumControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ForumDao forumDao;

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
        forumDao.deleteAll();
        userDao.deleteAll();
    }

    @Test
    @Order(1)
    void create() throws Exception {
        ForumSaveDto goodsSaveDto = new ForumSaveDto("Just Forum", "Keyboard for gamers.", "test@gmail.com");
        MockHttpServletRequestBuilder mockRequest = post("/sm/forums")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(goodsSaveDto));
        mockMvc.perform(mockRequest).andExpect(status().isCreated()).andExpect(jsonPath("$").doesNotExist());
        Forum goods = forumDao.findByName(goodsSaveDto.name()).orElseThrow();
        assertThat(goods.getId()).isGreaterThan(0L);
        assertEquals(goodsSaveDto.name(), goods.getName());
        assertEquals(goodsSaveDto.text(), goods.getText());
    }

    @Test
    @Order(2)
    void getById() throws Exception {
        MockHttpServletRequestBuilder mockRequest = get("/sm/forums/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Just Forum"))
                .andExpect(jsonPath("$.text").value("Keyboard for gamers."));
    }

    @Test
    @Order(3)
    void getAll() throws Exception {
        ForumSaveDto goodsSaveDto = new ForumSaveDto("Forum23", "Not bad", "test@gmail.com");
        mockMvc.perform(post("/sm/forums")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(goodsSaveDto))).andExpect(status().isCreated());
        MockHttpServletRequestBuilder mockRequest = get("/sm/forums")
                .param("page", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Just Forum"))
                .andExpect(jsonPath("$.content[1].name").value("Forum23"))
                .andExpect(jsonPath("$.numberOfElements").value(2));
    }

    @Test
    @Order(4)
    void delete_success() throws Exception {
        MockHttpServletRequestBuilder mockRequest = delete("/sm/forums/{id}", 1)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isNoContent()).andExpect(jsonPath("$").doesNotExist());
        MockHttpServletRequestBuilder mockRequestGetAll = get("/sm/forums")
                .param("page", String.valueOf(0))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequestGetAll).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Forum23"))
                .andExpect(jsonPath("$.numberOfElements").value(1));
    }

    @Test
    @Order(5)
    void update() throws Exception {
        ForumSaveDto goodsSaveDto = new ForumSaveDto("Better Forum", "Some text", "test@gmail.com");
        MockHttpServletRequestBuilder mockRequest = put("/sm/forums/{id}", 2)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(goodsSaveDto));
        mockMvc.perform(mockRequest).andExpect(status().isAccepted()).andExpect(jsonPath("$").doesNotExist());
        Forum goods = forumDao.findByName(goodsSaveDto.name()).orElseThrow();
        assertThat(goods.getId()).isEqualTo(2L);
        assertEquals(goodsSaveDto.name(), goods.getName());
        assertEquals(goodsSaveDto.text(), goods.getText());
    }
}
