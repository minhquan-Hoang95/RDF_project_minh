package fr.pir.configuration.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import fr.pir.model.security.CustomUserDetails;
import fr.pir.model.user.Role;
import fr.pir.model.user.User;
import fr.pir.service.rdf.AnnotationService;
import fr.pir.service.security.CustomUserDetailsService;
import fr.pir.service.security.JwtUtilService;

/**
 * Integration tests for the JWT RequestFilterConfig.
 * Verifies the security fix: protected endpoints require a valid token.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RequestFilterConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtilService jwtUtilService;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private AnnotationService annotationService;

    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setName("USER");

        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setActivate(true);
        user.setRole(role);

        userDetails = CustomUserDetails.build(user);
    }

    // ── Public endpoints — no token needed ─────────────────────────────────

    @Test
    void publicHealthEndpoint_noToken_returns200() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk());
    }

    @Test
    void publicAuthEndpoint_noToken_isReachable() throws Exception {
        // /api/auth is public — should not be blocked by the filter.
        // We expect 405 (method not allowed for GET on a POST-only endpoint),
        // not 401 (unauthorized).
        mockMvc.perform(get("/api/auth"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assert status != 401 : "Public endpoint should not return 401";
                });
    }

    // ── Protected endpoints — no token ────────────────────────────────────

    @Test
    void protectedEndpoint_noToken_returns401() throws Exception {
        mockMvc.perform(get("/api/annotation/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedCampaignEndpoint_noToken_returns401() throws Exception {
        mockMvc.perform(get("/api/campaign/all"))
                .andExpect(status().isUnauthorized());
    }

    // ── Protected endpoints — invalid token ───────────────────────────────

    @Test
    void protectedEndpoint_invalidToken_returns401() throws Exception {
        mockMvc.perform(get("/api/annotation/user")
                        .header("Authorization", "Bearer this.is.not.a.valid.jwt"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void protectedEndpoint_malformedBearerHeader_returns401() throws Exception {
        mockMvc.perform(get("/api/annotation/user")
                        .header("Authorization", "NotBearer something"))
                .andExpect(status().isUnauthorized());
    }

    // ── Protected endpoints — valid token ─────────────────────────────────

    @Test
    void protectedEndpoint_validToken_returns200() throws Exception {
        String token = jwtUtilService.generateToken("1");

        when(customUserDetailsService.loadUserById("1")).thenReturn(userDetails);
        when(annotationService.getAnnotationsByUser(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/annotation/user")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpoint_validToken_campaignAll_returns200() throws Exception {
        String token = jwtUtilService.generateToken("1");

        when(customUserDetailsService.loadUserById("1")).thenReturn(userDetails);
        when(annotationService.getAnnotationsByUser(any())).thenReturn(List.of());

        // CampaignService is not mocked here — we only care about the filter passing.
        // The request will reach the controller and fail at the service level (500),
        // but NOT be rejected by the filter (401).
        mockMvc.perform(get("/api/campaign/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assert status != 401 : "Valid token should not be rejected by the filter";
                });
    }
}
