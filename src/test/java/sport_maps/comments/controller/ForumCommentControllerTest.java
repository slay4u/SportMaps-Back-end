package sport_maps.comments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import sport_maps.comments.dao.ForumCommentDao;
import sport_maps.comments.domain.ForumComment;
import sport_maps.comments.dto.CommentSaveDto;
import sport_maps.nef.dao.ForumDao;
import sport_maps.nef.dto.ForumSaveDto;
import sport_maps.security.dao.UserDao;
import sport_maps.security.dao.VerificationTokenDao;
import sport_maps.security.domain.VerificationToken;
import sport_maps.security.dto.LoginRequest;
import sport_maps.security.dto.RegisterRequest;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ForumCommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ForumCommentDao eventCommentDao;

    @Autowired
    private ForumDao eventDao;

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
        ForumSaveDto goodsSaveDto = new ForumSaveDto("Just Forum", "Keyboard for gamers.", "test@gmail.com");
        MockHttpServletRequestBuilder mockRequest1 = post("/sm/forums")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(goodsSaveDto));
        mockMvc.perform(mockRequest1).andExpect(status().isCreated()).andExpect(jsonPath("$").doesNotExist());
    }

    @AfterAll
    void tearDown() {
        eventCommentDao.deleteAll();
        eventDao.deleteAll();
        userDao.deleteAll();
    }

    @Test
    @Order(1)
    void create() throws Exception {
        CommentSaveDto commentSaveDto = new CommentSaveDto("Event comment test", "test@gmail.com", 1L);
        MockHttpServletRequestBuilder mockRequest = post("/sm/forum-comments")
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commentSaveDto));
        mockMvc.perform(mockRequest).andExpect(status().isCreated()).andExpect(jsonPath("$").doesNotExist());
        ForumComment goods = eventCommentDao.findById(1L).orElseThrow();
        assertThat(goods.getId()).isGreaterThan(0L);
        assertEquals(commentSaveDto.text(), goods.getText());
    }

    @Test
    @Order(2)
    void update() throws Exception {
        CommentSaveDto commentSaveDto = new CommentSaveDto("Event comment test1 test1", "test@gmail.com", 1L);
        MockHttpServletRequestBuilder mockRequest = put("/sm/forum-comments/{id}", 1)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commentSaveDto));
        mockMvc.perform(mockRequest).andExpect(status().isAccepted()).andExpect(jsonPath("$").doesNotExist());
        ForumComment goods = eventCommentDao.findById(1L).orElseThrow();
        assertThat(goods.getId()).isEqualTo(1L);
        assertEquals(commentSaveDto.text(), goods.getText());
    }

    @Test
    @Order(3)
    void delete_success() throws Exception {
        MockHttpServletRequestBuilder mockRequest = delete("/sm/forum-comments/{id}", 1)
                .header("Authorization", "Bearer " + jwt)
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isNoContent()).andExpect(jsonPath("$").doesNotExist());
        assertThrows(NoSuchElementException.class, () -> eventCommentDao.findById(1L).orElseThrow());
    }
}
