package fr.pir.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Validation tests for authentication endpoints.
 * Verifies that @Valid constraints on request bodies return 400 for bad input.
 *
 * Filters are disabled (addFilters = false) so these tests focus purely on
 * Spring MVC input validation, not on the security filter chain.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class AuthControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    // ── Signup (@Valid User) ────────────────────────────────────────────────

    @Test
    void testSignupWithInvalidEmailReturns400() throws Exception {
        // Plain-text JSON so the controller receives the raw value, not a BCrypt hash.
        String body = "{\"email\":\"invalid-email\",\"password\":\"password123\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testSignupWithShortPasswordReturns400() throws Exception {
        String body = "{\"email\":\"valid@email.com\",\"password\":\"short\",\"firstName\":\"John\",\"lastName\":\"Doe\"}";

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testSignupWithMissingFirstNameReturns400() throws Exception {
        String body = "{\"email\":\"valid@email.com\",\"password\":\"password123\",\"firstName\":\"\",\"lastName\":\"Doe\"}";

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    // ── Login (@Valid LoginRequest) ─────────────────────────────────────────

    @Test
    void testLoginWithShortPasswordReturns400() throws Exception {
        String body = "{\"email\":\"test@example.com\",\"password\":\"short\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithInvalidEmailReturns400() throws Exception {
        String body = "{\"email\":\"invalid\",\"password\":\"password123\"}";

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andExpect(status().isBadRequest());
    }
}
