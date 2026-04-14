package fr.pir.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.pir.model.user.User;
import fr.pir.repository.user.RoleRepository;
import fr.pir.repository.user.UserRepository;
import fr.pir.service.email.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.mock.mockito.SpyBean;
import fr.pir.service.email.EmailTemplateService;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import jakarta.mail.MessagingException;
import java.io.IOException;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthControllerSignupTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @SpyBean
    private EmailService emailService;

    @MockBean
    private EmailTemplateService emailTemplateService;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    public void testSignupSuccess() throws Exception {
        // Mock email service to avoid 500 if mail server is not configured
        doNothing().when(emailService).sendEmailWithTemplate(anyString(), anyString(), any());

        // Ensure "USER" role exists
        if (roleRepository.findRoleByName("USER") == null) {
            fr.pir.model.user.Role role = new fr.pir.model.user.Role();
            role.setName("USER");
            roleRepository.save(role);
        }

        // Mock template service to return a valid template
        fr.pir.model.email.EmailTemplate template = new fr.pir.model.email.EmailTemplate();
        template.setName("new_account");
        template.setContent("Welcome!");
        when(emailTemplateService.getEmailTemplateByName("new_account")).thenReturn(template);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testSignupEmailTemplateNotFound() throws Exception {
        // Ensure USER role exists for this test
        if (roleRepository.findRoleByName("USER") == null) {
            fr.pir.model.user.Role role = new fr.pir.model.user.Role();
            role.setName("USER");
            roleRepository.save(role);
        }

        // Mock template service to return null, simulating a missing template in the DB
        when(emailTemplateService.getEmailTemplateByName(anyString())).thenReturn(null);

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        // Email failure is now handled gracefully: account is created and auto-activated.
        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testSignupRoleNotFound() throws Exception {
        // Mock role repository to return null for USER role
        // Need to mock or ensure it's empty
        roleRepository.deleteAll();

        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password123");

        // This will cause 500 if role is null and we try to set it
        mockMvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isInternalServerError());
    }
}
