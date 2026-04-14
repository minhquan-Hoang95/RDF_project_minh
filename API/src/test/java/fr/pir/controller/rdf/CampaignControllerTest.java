package fr.pir.controller.rdf;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.HashSet;

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

import fr.pir.exception.AccountException;
import fr.pir.exception.NotFoundException;
import fr.pir.model.rdf.Campaign;
import fr.pir.model.user.User;
import fr.pir.service.rdf.CampaignService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Integration tests for CampaignController.
 * Filters are disabled — security is tested separately in RequestFilterConfigTest.
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CampaignService campaignService;

    private Campaign campaign;
    private User creator;

    @BeforeEach
    void setUp() {
        creator = new User();
        creator.setId(1L);
        creator.setEmail("creator@example.com");

        campaign = new Campaign();
        campaign.setId(10L);
        campaign.setName("Test Campaign");
        campaign.setSelectedThesauri(Collections.singletonList("DBpedia"));
        campaign.setCreator(creator);
        campaign.setParticipants(new HashSet<>());
    }

    // ── POST /api/campaign ──────────────────────────────────────────────────

    @Test
    void createCampaign_returns201WithBody() throws Exception {
        when(campaignService.createCampaign(any(Campaign.class), any(HttpServletRequest.class)))
                .thenReturn(campaign);

        mockMvc.perform(post("/api/campaign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campaign)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("Test Campaign"));
    }

    @Test
    void createCampaign_duplicateName_returns409() throws Exception {
        when(campaignService.createCampaign(any(Campaign.class), any(HttpServletRequest.class)))
                .thenThrow(new org.springframework.dao.DataIntegrityViolationException(
                        "duplicate key value violates unique constraint; Key (name)=(Test Campaign)"));

        mockMvc.perform(post("/api/campaign")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(campaign)))
                .andExpect(status().isConflict());
    }

    // ── GET /api/campaign?id=N ──────────────────────────────────────────────

    @Test
    void getCampaignById_returns200WhenFound() throws Exception {
        when(campaignService.getCampaignById(anyLong(), any(HttpServletRequest.class)))
                .thenReturn(campaign);

        mockMvc.perform(get("/api/campaign").param("id", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Campaign"));
    }

    @Test
    void getCampaignById_returns404WhenNotFound() throws Exception {
        when(campaignService.getCampaignById(anyLong(), any(HttpServletRequest.class)))
                .thenThrow(new NotFoundException("CampaignNotFound"));

        mockMvc.perform(get("/api/campaign").param("id", "999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getCampaignById_returns401WhenNotMember() throws Exception {
        when(campaignService.getCampaignById(anyLong(), any(HttpServletRequest.class)))
                .thenThrow(new AccountException("UserNotAuthorized"));

        mockMvc.perform(get("/api/campaign").param("id", "10"))
                .andExpect(status().isUnauthorized());
    }

    // ── GET /api/campaign/all ───────────────────────────────────────────────

    @Test
    void getAllCampaigns_returns200WithList() throws Exception {
        when(campaignService.getAllCampaignsByUser(any(HttpServletRequest.class)))
                .thenReturn(Collections.singletonList(campaign));

        mockMvc.perform(get("/api/campaign/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Campaign"));
    }

    @Test
    void getAllCampaigns_emptyList_returns200() throws Exception {
        when(campaignService.getAllCampaignsByUser(any(HttpServletRequest.class)))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/campaign/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ── POST /api/campaign/invite ───────────────────────────────────────────

    @Test
    void inviteParticipant_returns200() throws Exception {
        when(campaignService.inviteParticipant(anyLong(), anyString(), any(HttpServletRequest.class)))
                .thenReturn("InvitationSend");

        mockMvc.perform(post("/api/campaign/invite")
                        .param("campaignId", "10")
                        .param("participantEmail", "participant@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void inviteParticipant_campaignNotFound_returns404() throws Exception {
        when(campaignService.inviteParticipant(anyLong(), anyString(), any(HttpServletRequest.class)))
                .thenThrow(new NotFoundException("CampaignNotFound"));

        mockMvc.perform(post("/api/campaign/invite")
                        .param("campaignId", "999")
                        .param("participantEmail", "participant@example.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    void inviteParticipant_notCreator_returns401() throws Exception {
        when(campaignService.inviteParticipant(anyLong(), anyString(), any(HttpServletRequest.class)))
                .thenThrow(new AccountException("UserNotAuthorized"));

        mockMvc.perform(post("/api/campaign/invite")
                        .param("campaignId", "10")
                        .param("participantEmail", "participant@example.com"))
                .andExpect(status().isUnauthorized());
    }

    // ── GET /api/campaign/accept ─────────────────────────────────────────────

    @Test
    void acceptInvitation_returns200() throws Exception {
        when(campaignService.acceptInvitation(anyString(), anyString()))
                .thenReturn("Invitation accepted.");

        mockMvc.perform(get("/api/campaign/accept")
                        .param("participantEmail", "participant@example.com")
                        .param("campaignName", "Test Campaign"))
                .andExpect(status().isOk());
    }

    @Test
    void acceptInvitation_campaignNotFound_returns404() throws Exception {
        when(campaignService.acceptInvitation(anyString(), anyString()))
                .thenThrow(new NotFoundException("CampaignNotFound"));

        mockMvc.perform(get("/api/campaign/accept")
                        .param("participantEmail", "participant@example.com")
                        .param("campaignName", "Unknown"))
                .andExpect(status().isNotFound());
    }
}
