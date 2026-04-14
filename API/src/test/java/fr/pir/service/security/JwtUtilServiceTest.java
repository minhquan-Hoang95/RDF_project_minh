package fr.pir.service.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtUtilServiceTest {

    private JwtUtilService jwtUtilService;

    @Mock
    private UserDetails userDetails;

    private static final String USER_ID = "42";
    private static final String SECRET = "JWTSecretKeyForTestingHmacSHA512NeedsAtLeast64BytesForSecurity1!";

    @BeforeEach
    void setUp() {
        SecretKey key = new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        jwtUtilService = new JwtUtilService(key);
        ReflectionTestUtils.setField(jwtUtilService, "jwtExpiration", 3600);
    }

    @Test
    void generateToken_returnsNonNullToken() {
        String token = jwtUtilService.generateToken(USER_ID);
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void getIdFromToken_returnsCorrectSubject() {
        String token = jwtUtilService.generateToken(USER_ID);
        String extracted = jwtUtilService.getIdFromToken(token);
        assertEquals(USER_ID, extracted);
    }

    @Test
    void getIdFromAuthorization_stripsBearer() {
        String token = jwtUtilService.generateToken(USER_ID);
        String extracted = jwtUtilService.getIdFromAuthorization("Bearer " + token);
        assertEquals(USER_ID, extracted);
    }

    @Test
    void validateToken_trueForValidToken() {
        String token = jwtUtilService.generateToken(USER_ID);
        when(userDetails.getUsername()).thenReturn(USER_ID);

        assertTrue(jwtUtilService.validateToken(token, userDetails));
    }

    @Test
    void validateToken_falseWhenUsernameMismatch() {
        String token = jwtUtilService.generateToken(USER_ID);
        when(userDetails.getUsername()).thenReturn("999");

        assertFalse(jwtUtilService.validateToken(token, userDetails));
    }

    @Test
    void validateToken_falseForExpiredToken() {
        // Set expiration to -1 second so the token is already expired
        ReflectionTestUtils.setField(jwtUtilService, "jwtExpiration", -1);
        String token = jwtUtilService.generateToken(USER_ID);
        // No stub needed: validateToken catches ExpiredJwtException and returns false immediately
        assertFalse(jwtUtilService.validateToken(token, userDetails));
    }

    @Test
    void getExpirationDateFromToken_notNull() {
        String token = jwtUtilService.generateToken(USER_ID);
        assertNotNull(jwtUtilService.getExpirationDateFromToken(token));
    }
}
