package fr.pir.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pir.model.user.User;
import fr.pir.service.user.UserService;
import fr.pir.service.user.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerForbiddenTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthService authService;

    @Test
    void testSignupIsAccessible() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userService.signup(any(User.class))).thenReturn("Success");

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isCreated());
    }

    @Test
    void testLoginIsAccessible() throws Exception {
        String loginRequest = "{\"email\": \"test@example.com\", \"password\": \"password123\"}";

        when(authService.login(any(), any(), any())).thenReturn("mock-token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
            .andExpect(status().isOk());
    }
}
