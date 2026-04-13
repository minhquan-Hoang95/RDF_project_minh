package fr.pir.service.rdf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.security.auth.login.AccountException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.pir.exception.NotFoundException;
import fr.pir.model.rdf.Annotation;
import fr.pir.model.user.User;
import fr.pir.repository.rdf.AnnotationRepository;
import fr.pir.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class AnnotationServiceTest {

    @Mock
    private AnnotationRepository annotationRepository;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AnnotationService annotationService;

    private User user;
    private Annotation annotation;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        annotation = new Annotation();
        annotation.setId(10L);
        annotation.setDescription("Old Description");
        annotation.setCreator(user);
    }

    @Test
    void testUpdateAnnotationDescription_Success() throws Exception {
        // Arrange
        String newDescription = "New Updated Description";
        when(userService.getUserFromToken(request)).thenReturn(user);
        when(annotationRepository.findById(10L)).thenReturn(Optional.of(annotation));

        // Act
        Annotation updatedAnnotation = annotationService.updateAnnotation(10L, newDescription, request);

        // Assert
        assertNotNull(updatedAnnotation);
        assertEquals(newDescription, updatedAnnotation.getDescription());
        verify(annotationRepository).save(annotation);
    }

    @Test
    void testUpdateAnnotation_NotFound() throws NotFoundException {
        // Arrange
        when(userService.getUserFromToken(request)).thenReturn(user);
        when(annotationRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            annotationService.updateAnnotation(99L, "New Description", request);
        });
    }

    @Test
    void testUpdateAnnotation_Unauthorized() throws NotFoundException {
        // Arrange
        User anotherUser = new User();
        anotherUser.setId(2L);
        
        when(userService.getUserFromToken(request)).thenReturn(user);
        when(annotationRepository.findById(10L)).thenReturn(Optional.of(annotation));
        annotation.setCreator(anotherUser); // Annotation owned by someone else

        // Act & Assert
        assertThrows(AccountException.class, () -> {
            annotationService.updateAnnotation(10L, "New Description", request);
        });
    }
}
