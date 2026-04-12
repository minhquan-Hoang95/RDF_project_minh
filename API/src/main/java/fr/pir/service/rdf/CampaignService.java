package fr.pir.service.rdf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.exception.AccountException;
import fr.pir.exception.NotFoundException;
import fr.pir.model.rdf.Campaign;
import fr.pir.model.user.User;
import fr.pir.repository.rdf.CampaignRepository;
import fr.pir.service.user.UserService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class CampaignService {

    private static final Logger L = LogManager.getLogger(CampaignService.class);

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private UserService userService;

    public Campaign getCampaignById(Long id, HttpServletRequest request) throws Exception {
        L.debug("id : {}", id);

        Campaign campaign = this.campaignRepository.findById(id).orElse(null);

        if (campaign == null) {
            L.error("CampaignNotFound : {}", id);

            throw new NotFoundException("CampaignNotFound");
        }

        User user = this.userService.getUserFromToken(request);

        if (!user.getId().equals(campaign.getCreator().getId()) && !campaign.getParticipants().contains(user)) {
            L.error("User {} is not creator or participant in campaign {}", user.getId(), id);

            throw new AccountException("UserNotAuthorized");
        }

        return campaign;
    }

    public List<Campaign> getAllCampaignsByUser(HttpServletRequest request) throws NotFoundException {
        L.debug("");

        User user = this.userService.getUserFromToken(request);

        return this.campaignRepository.findAllCampaignsByCreatorOrParticipants(user);
    }

    @Transactional
    public Campaign createCampaign(Campaign campaign, HttpServletRequest request) throws NotFoundException {
        L.debug("name : {}", campaign.getName());

        User user = this.userService.getUserFromToken(request);
        campaign.setCreator(user);

        this.campaignRepository.save(campaign);

        this.userService.addCampaignToAdmin(user.getId(), campaign);

        return campaign;
    }

    @Transactional
    public String inviteParticipant(Long campaignId, String participantEmail, HttpServletRequest request)
            throws AccountException, NotFoundException, MessagingException, IOException {
        L.debug("campaignId : {}, participantEmail : {}", campaignId, participantEmail);

        Campaign campaign = this.campaignRepository.findById(campaignId).orElse(null);

        if (campaign == null) {
            L.error("CampaignNotFound : {}", campaignId);

            throw new NotFoundException("CampaignNotFound");
        }

        User user = this.userService.getUserFromToken(request);

        if (!user.getId().equals(campaign.getCreator().getId())) {
            L.error("User {} is not creator of campaign {}", user.getId(), campaignId);

            throw new AccountException("UserNotAuthorized");
        }

        User participant = this.userService.getUserByEmail(participantEmail);

        if (participant == null) {
            L.error("ParticipantNotFound : {}", participantEmail);

            throw new NotFoundException("ParticipantNotFound");
        }

        if (campaign.getParticipants().contains(participant)) {
            L.error("ParticipantAlreadyInCampaign : {}", participantEmail);

            throw new AccountException("ParticipantAlreadyInCampaign");
        }

        this.userService.addInvitationToParticipant(participant.getId(), campaign);

        return "InvitationSend";
    }

    @Transactional
    public String acceptInvitation(String participantEmail, String campaignName)
            throws NotFoundException, AccountException, MessagingException, IOException {
        L.debug("campaignName : {}, participantEmail : {}", campaignName, participantEmail);

        Campaign campaign = this.campaignRepository.findByName(campaignName).orElse(null);

        if (campaign == null) {
            L.error("CampaignNotFound : {}", campaignName);

            throw new NotFoundException("CampaignNotFound");
        }

        this.userService.acceptInvitation(participantEmail, campaign);

        return "Invitation accepted.";
    }

    private File createTurtleFile(Campaign campaign) throws IOException, InterruptedException {
        L.debug("campaign name : {}", campaign.getName());

        // Convert campaign to JSON string
        ObjectMapper mapper = new ObjectMapper();
        String campaignJson = mapper.writeValueAsString(campaign);

        // Write to temp file
        File tempJsonFile = File.createTempFile("campaign", ".json");

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(tempJsonFile),
                StandardCharsets.UTF_8)) {
            writer.write(campaignJson);
        }

        String scriptPath = Paths.get("json2rdf.py").toAbsolutePath().toString();

        String pythonCmd = System.getProperty("os.name").toLowerCase().contains("win") ? "python" : "python3";
        ProcessBuilder pb = new ProcessBuilder(pythonCmd, scriptPath, "-i", tempJsonFile.getAbsolutePath());

        Process process = pb.start();

        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String errorLine;

        while ((errorLine = errorReader.readLine()) != null) {
            L.error("PYTHON ERROR: {}", errorLine);
        }

        int exitCode = process.waitFor();

        if (exitCode != 0) {
            L.error("Error executing script, exit code: {}", exitCode);

            throw new IOException("Error executing script.");
        }

        return new File("campaign.ttl");
    }

    public File getTurtleCampaign(Long id, HttpServletRequest request) throws Exception {
        L.debug("id : {}", id);

        Campaign campaign = this.getCampaignById(id, request);

        return this.createTurtleFile(campaign);
    }

}
