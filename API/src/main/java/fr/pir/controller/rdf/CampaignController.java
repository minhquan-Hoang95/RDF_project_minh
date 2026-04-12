package fr.pir.controller.rdf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.exception.AccountException;
import fr.pir.exception.NotFoundException;
import fr.pir.model.rdf.Campaign;
import fr.pir.service.rdf.CampaignService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/campaign")
public class CampaignController {

    private static final Logger L = LogManager.getLogger(CampaignController.class);

    @Autowired
    private CampaignService campaignService;

    @PostMapping
    public ResponseEntity<?> createCampaign(@RequestBody Campaign campaign, HttpServletRequest request) {
        L.debug("name : {}", campaign.getName());

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.campaignService.createCampaign(campaign, request));
        } catch (DataIntegrityViolationException e) {
            L.error("An exception is raised : {}", e.getMessage());

            String error = e.getMessage();

            if (error.contains("duplicate key")) {
                if (error.contains("Key (name)=")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate key name.");
                }
            }

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data integrity violation.");
        } catch (Exception e) {
            L.error("Error creating campaign", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getCampaignById(@RequestParam Long id, HttpServletRequest request) {
        L.debug("id : {}", id);

        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.campaignService.getCampaignById(id, request));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccountException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            L.error("Error getting campaign", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCampaignsByUser(HttpServletRequest request) {
        L.debug("");

        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.campaignService.getAllCampaignsByUser(request));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            L.error("Error getting all campaigns", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    @PostMapping("/invite")
    public ResponseEntity<String> inviteParticipant(@RequestParam Long campaignId,
            @RequestParam String participantEmail,
            HttpServletRequest request) {
        L.debug("campaignId: {}, participantEmail: {}", campaignId, participantEmail);

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.campaignService.inviteParticipant(campaignId, participantEmail, request));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccountException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (DataIntegrityViolationException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Participant already invited.");
        } catch (Exception e) {
            L.error("Error inviting participant to campaign", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    @GetMapping("/accept")
    public ResponseEntity<String> acceptInvitation(@RequestParam String participantEmail,
            @RequestParam String campaignName) {
        L.debug("campaignName: {}, participantEmail: {}", campaignName, participantEmail);

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.campaignService.acceptInvitation(participantEmail, campaignName));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            L.error("Error accepting invitation to campaign", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    @GetMapping("/export")
    public ResponseEntity<?> exportCampaign(@RequestParam Long id, HttpServletRequest request) {
        L.debug("campaign id: {}", id);

        try {
            File ttl = this.campaignService.getTurtleCampaign(id, request);

            Resource resource = new FileSystemResource(ttl);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"campaign.ttl\"")
                    .contentLength(ttl.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (AccountException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            L.error("Error exporting campaign", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

}
