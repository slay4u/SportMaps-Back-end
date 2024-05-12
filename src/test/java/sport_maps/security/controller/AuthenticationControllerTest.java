package sport_maps.security.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import sport_maps.security.dao.UserDao;
import sport_maps.security.dao.VerificationTokenDao;
import sport_maps.security.domain.User;
import sport_maps.security.domain.VerificationToken;
import sport_maps.security.dto.LoginRequest;
import sport_maps.security.dto.RegisterRequest;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserDao userDao;

    @Autowired
    private VerificationTokenDao verificationTokenDao;

    private String jwt;
    private String refresh;

    @AfterAll
    public void tearDown() {
        userDao.deleteAll();
    }

    @Test
    @Order(1)
    public void success_signUp() throws Exception {
        RegisterRequest signUpDto = new RegisterRequest("Test", "User", "test@gmail.com", "Test12345^");
        MockHttpServletRequestBuilder mockRequest = post("/sm/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signUpDto));
        mockMvc.perform(mockRequest).andExpect(status().isCreated());
        User user = userDao.findByEmail("test@gmail.com").orElseThrow();
        assertThat(user.getId()).isGreaterThan(0L);
        assertEquals(user.getFirstName(), signUpDto.firstName());
        assertEquals(user.getEmail(), signUpDto.email());
    }

    @Test
    @Order(2)
    public void validationCheck_signUp() throws Exception {
        RegisterRequest existingUser = new RegisterRequest("Test", "User", "test@gmail.com", "Test12345^");
        RegisterRequest wrongFN = new RegisterRequest("", "User", "test@gmail.com", "Test12345^");
        RegisterRequest wrongLN = new RegisterRequest("Test", "", "test@gmail.com", "Test12345^");
        RegisterRequest wrongE = new RegisterRequest("Test", "User", "", "Test12345^");
        RegisterRequest wrongP = new RegisterRequest("Test", "User", "test@gmail.com", "");
        MockHttpServletRequestBuilder mockRequestExisting = post("/sm/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(existingUser));
        MockHttpServletRequestBuilder mockRequestFN = post("/sm/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(wrongFN));
        MockHttpServletRequestBuilder mockRequestLN = post("/sm/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(wrongLN));
        MockHttpServletRequestBuilder mockRequestE = post("/sm/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(wrongE));
        MockHttpServletRequestBuilder mockRequestP = post("/sm/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(wrongP));
        mockMvc.perform(mockRequestExisting).andExpect(status().isBadRequest());
        mockMvc.perform(mockRequestFN).andExpect(status().isBadRequest());
        mockMvc.perform(mockRequestLN).andExpect(status().isBadRequest());
        mockMvc.perform(mockRequestE).andExpect(status().isBadRequest());
        mockMvc.perform(mockRequestP).andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    void login_fail_NotEnabled() throws Exception {
        LoginRequest signInDto = new LoginRequest("test@gmail.com", "Test12345^");
        MockHttpServletRequestBuilder mockRequest = post("/sm/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signInDto));
        mockMvc.perform(mockRequest).andExpect(status().isUnauthorized());
    }

    @Test
    @Order(4)
    public void verifyAccount() throws Exception {
        VerificationToken verificationToken = verificationTokenDao.findAll().getFirst();
        MockHttpServletRequestBuilder mockRequest = get("/sm/auth/" + verificationToken.getToken())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest).andExpect(status().isAccepted());
        assertTrue(userDao.findByEmail("test@gmail.com").orElseThrow().isEnabled());
    }

    @Test
    @Order(5)
    void login_fail_Bad_Credentials() throws Exception {
        LoginRequest signInDto = new LoginRequest("test@gmail.com", "Test12345^+");
        MockHttpServletRequestBuilder mockRequest = post("/sm/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signInDto));
        mockMvc.perform(mockRequest).andExpect(status().isUnauthorized());
    }

    @Test
    @Order(6)
    public void login_success() throws Exception {
        LoginRequest signInDto = new LoginRequest("test@gmail.com", "Test12345^");
        MockHttpServletRequestBuilder mockRequest = post("/sm/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(signInDto));
        MvcResult result = mockMvc.perform(mockRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(cookie().exists("user_info"))
                .andExpect(cookie().httpOnly("user_info", true))
                .andReturn();
        assertThat(result.getResponse().getContentAsString().length()).isGreaterThan(155);
        this.jwt = result.getResponse().getContentAsString();
        this.refresh = Objects.requireNonNull(result.getResponse().getCookie("user_info")).getValue();
    }

    @Test
    @Order(7)
    public void refresh() throws Exception {
        MockHttpServletRequestBuilder mockRequestRefresh = post("/sm/auth")
                .header("Authorization", "Bearer " + jwt)
                .cookie(new Cookie("user_info", refresh))
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult refreshResult = mockMvc.perform(mockRequestRefresh).andExpect(status().isResetContent())
                .andExpect(jsonPath("$").isNotEmpty()).andReturn();
        String newJwt = refreshResult.getResponse().getContentAsString();
        assertThat(newJwt.length()).isGreaterThan(155);
        assertThat(newJwt).isNotSameAs(jwt);
        this.jwt = newJwt;
    }

    @Test
    @Order(8)
    public void logout() throws Exception {
        MockHttpServletRequestBuilder mockRequestRefresh = delete("/sm/auth")
                .header("Authorization", "Bearer " + jwt)
                .cookie(new Cookie("user_info", refresh))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequestRefresh).andExpect(status().isNoContent()).andDo(print())
                .andExpect(cookie().maxAge("user_info", 0))
                .andExpect(jsonPath("$").doesNotExist());
    }
}