package fr.pir.controller.rdf;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.pir.exception.NotFoundException;
import fr.pir.model.rdf.Annotation;
import fr.pir.service.rdf.AnnotationService;
import jakarta.servlet.http.HttpServletRequest;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AnnotationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnnotationService annotationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Annotation annotation;

    @BeforeEach
    void setUp() {
        annotation = new Annotation();
        annotation.setId(1L);
        annotation.setDescription("Test Annotation");
    }

    @Test
    void testCreateAnnotation_Success() throws Exception {
        when(annotationService.createAnnotation(any(Annotation.class), any(HttpServletRequest.class)))
                .thenReturn(annotation);

        mockMvc.perform(post("/api/annotation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(annotation)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test Annotation"));
    }

    @Test
    void testUpdateAnnotation_Success() throws Exception {
        annotation.setDescription("Updated Description");
        when(annotationService.updateAnnotation(eq(1L), eq("Updated Description"), any(HttpServletRequest.class)))
                .thenReturn(annotation);

        mockMvc.perform(patch("/api/annotation")
                .param("id", "1")
                .param("description", "Updated Description"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Description"));
    }

    @Test
    void testUpdateAnnotation_NotFound() throws Exception {
        when(annotationService.updateAnnotation(anyLong(), anyString(), any(HttpServletRequest.class)))
                .thenThrow(new NotFoundException("Annotation not found"));

        mockMvc.perform(patch("/api/annotation")
                .param("id", "99")
                .param("description", "Something"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAnnotationsByUser_Success() throws Exception {
        when(annotationService.getAnnotationsByUser(any(HttpServletRequest.class)))
                .thenReturn(Collections.singletonList(annotation));

        mockMvc.perform(get("/api/annotation/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
