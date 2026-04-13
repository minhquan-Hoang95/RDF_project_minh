package fr.pir.service.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.pir.exception.NotFoundException;
import fr.pir.model.user.User;
import fr.pir.repository.user.UserRepository;
import fr.pir.service.security.JwtUtilService;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtilService jwtUtilService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
    }

    @Test
    void testGetUserById_Success() throws NotFoundException {
        when(userRepository.findUserById(1L)).thenReturn(user);

        User found = userService.getUserById("1");

        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findUserById(anyLong())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            userService.getUserById("99");
        });
    }

    @Test
    void testGetUserByEmail_Success() throws NotFoundException {
        when(userRepository.findUserByEmail("test@example.com")).thenReturn(user);

        User found = userService.getUserByEmail("test@example.com");

        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    @Test
    void testGetUserFromToken_Success() throws NotFoundException {
        when(request.getHeader("Authorization")).thenReturn("Bearer mock-token");
        when(jwtUtilService.getIdFromToken("mock-token")).thenReturn("1");
        when(userRepository.findUserById(1L)).thenReturn(user);

        User found = userService.getUserFromToken(request);

        assertNotNull(found);
        assertEquals(1L, found.getId());
    }
}
