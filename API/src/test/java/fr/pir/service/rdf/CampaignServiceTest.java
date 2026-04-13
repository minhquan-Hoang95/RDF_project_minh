package fr.pir.service.rdf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.pir.exception.AccountException;
import fr.pir.exception.NotFoundException;
import fr.pir.model.rdf.Campaign;
import fr.pir.model.user.User;
import fr.pir.repository.rdf.CampaignRepository;
import fr.pir.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class CampaignServiceTest {

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CampaignService campaignService;

    private User user;
    private Campaign campaign;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        campaign = new Campaign();
        campaign.setId(10L);
        campaign.setName("Test Campaign");
        campaign.setCreator(user);
        campaign.setParticipants(new HashSet<>());
    }

    @Test
    void testGetCampaignById_Success() throws Exception {
        when(campaignRepository.findById(10L)).thenReturn(Optional.of(campaign));
        when(userService.getUserFromToken(request)).thenReturn(user);

        Campaign found = campaignService.getCampaignById(10L, request);

        assertNotNull(found);
        assertEquals("Test Campaign", found.getName());
    }

    @Test
    void testGetCampaignById_NotFound() {
        when(campaignRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            campaignService.getCampaignById(99L, request);
        });
    }

    @Test
    void testGetCampaignById_Unauthorized() throws Exception {
        User anotherUser = new User();
        anotherUser.setId(2L);
        
        when(campaignRepository.findById(10L)).thenReturn(Optional.of(campaign));
        when(userService.getUserFromToken(request)).thenReturn(anotherUser);

        assertThrows(AccountException.class, () -> {
            campaignService.getCampaignById(10L, request);
        });
    }

    @Test
    void testCreateCampaign_Success() throws NotFoundException {
        when(userService.getUserFromToken(request)).thenReturn(user);

        Campaign newCampaign = new Campaign();
        newCampaign.setName("New Campaign");

        Campaign created = campaignService.createCampaign(newCampaign, request);

        assertNotNull(created);
        assertEquals(user, created.getCreator());
        verify(campaignRepository).save(newCampaign);
        verify(userService).addCampaignToAdmin(user.getId(), newCampaign);
    }
}
