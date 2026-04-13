package fr.pir.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pir.model.user.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test suite for authentication validation
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSignupWithInvalidEmailReturns400() throws Exception {
        User user = new User();
        user.setEmail("invalid-email");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testSignupWithShortPasswordReturns400() throws Exception {
        User user = new User();
        user.setEmail("valid@email.com");
        user.setPassword("short");
        user.setFirstName("John");
        user.setLastName("Doe");

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testSignupWithMissingFirstNameReturns400() throws Exception {
        User user = new User();
        user.setEmail("valid@email.com");
        user.setPassword("password123");
        user.setFirstName("");
        user.setLastName("Doe");

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithShortPasswordReturns400() throws Exception {
        String loginRequest = "{\"email\": \"test@example.com\", \"password\": \"short\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithInvalidEmailReturns400() throws Exception {
        String loginRequest = "{\"email\": \"invalid\", \"password\": \"password123\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginRequest))
            .andExpect(status().isBadRequest());
    }
}

