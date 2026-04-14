package fr.pir.service.user;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.pir.exception.EmailException;
import fr.pir.exception.NotFoundException;
import fr.pir.model.rdf.Campaign;
import fr.pir.model.rdf.Invitation;
import fr.pir.model.user.Role;
import fr.pir.model.user.User;
import fr.pir.model.user.UserTableInterface;
import fr.pir.repository.rdf.InvitationRepository;
import fr.pir.repository.user.RoleRepository;
import fr.pir.repository.user.UserRepository;
import fr.pir.service.email.EmailService;
import fr.pir.service.security.JwtUtilService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class UserService {

    private static final Logger L = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private JwtUtilService jwtUtilService;

    private final String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    private boolean properUser(User user) throws EmailException {
        L.debug("email : {}", user.getEmail());

        if (!user.getEmail().matches(this.emailRegex)) {
            L.error("Incorrect email : {}", user.getEmail());

            throw new EmailException("Incorrect email.");
        }

        return true;
    }

    public User getUserFromToken(HttpServletRequest request) throws NotFoundException {
        L.debug("");

        String jwt = request.getHeader("Authorization");
        jwt = jwt.replace("Bearer ", "");

        String id = this.jwtUtilService.getIdFromToken(jwt);

        return this.getUserById(id);
    }

    public User getUserByEmail(String email) throws NotFoundException {
        L.debug("email : {}", email);

        User user = this.userRepository.findUserByEmail(email);

        if (user == null) {
            L.error("Incorrect email : {}", email);

            throw new NotFoundException("Incorrect email.");
        }

        return user;
    }

    public User getUserById(String id) throws NotFoundException {
        L.debug("id : {}", id);

        long idConverted = Long.valueOf(id);

        User user = this.userRepository.findUserById(idConverted);

        if (user == null) {
            L.error("Incorrect id : {}", id);

            throw new NotFoundException("Incorrect id.");
        }

        return user;
    }

    public List<UserTableInterface> getUsers() {
        L.debug("");

        return this.userRepository.findUsersForInterface();
    }

    public List<User> getAdminsWithoutCurrentUser(Long id) throws NotFoundException {
        L.debug("id : {}", id);

        return this.userRepository.findAdminsWithoutCurrentUser(id);
    }

    @Transactional
    public String signup(User user) throws Exception {
        L.debug("email : {}", user.getEmail());

        this.properUser(user);

        String rawPwd = user.getRawPassword();
        if (rawPwd == null || rawPwd.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }

        String firstName = user.getFirstName();
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name is required");
        }

        Role role = this.roleRepository.findRoleByName("USER");

        if (role == null) {
            L.error("Default USER role not found in database");
            throw new IllegalStateException("Default USER role not found in database");
        }

        user.setRole(role);

        this.userRepository.save(user);

        try {
            this.verificationCodeService.createAndSendVerificationCode(user.getEmail(), "new_account");
        } catch (Exception e) {
            L.warn("Email service unavailable ({}). Activating account without email verification.", e.getMessage());
            user.setActivate(true);
            this.userRepository.save(user);
            return "Account created. Email verification is currently unavailable — you can log in directly.";
        }

        return "Verification code sent to email.";
    }

    @Transactional
    public String passwordForgotten(String email) throws Exception {
        L.debug("email : {}", email);

        User user = this.getUserByEmail(email);

        user.setPasswordWithoutBCrypt("***");

        this.userRepository.save(user);
        user.setActivate(false);

        this.verificationCodeService.createAndSendVerificationCode(email, "password_forgotten");

        return "Verification code sent to email.";
    }

    @Transactional
    public String newPassword(String email, String code, String password, HttpServletRequest request) throws Exception {
        L.debug("email : {}", email);

        this.verificationCodeService.correctVerificationCode(email, code);

        User user = this.userRepository.findUserByEmail(email);

        user.setPassword(password);
        user.setActivate(true);

        this.userRepository.save(user);

        return "Password changed.";
    }

    @Transactional
    public String validateAccount(String email, String code) throws Exception {
        L.debug("email : {}", email);

        User user = this.getUserByEmail(email);

        this.verificationCodeService.correctVerificationCode(email, code);

        user.setActivate(true);

        this.userRepository.save(user);

        return "Account validated.";
    }

    @Transactional
    public List<User> addCampaignToAdmin(Long id, Campaign campaign) {
        L.debug("user id : {}, campaign : {}", id, campaign.getName());

        List<User> admins = this.userRepository.findAdminsWithoutCurrentUser(id);

        for (User admin : admins) {
            if (admin.getId() != id) {
                admin.getCampaignsParticipated().add(campaign);
                this.userRepository.save(admin);
            }
        }

        return admins;
    }

    @Transactional
    public User addInvitationToParticipant(Long id, Campaign campaign) throws MessagingException, IOException {
        L.debug("user id : {}, campaign : {}", id, campaign.getName());

        User user = this.userRepository.findUserById(id);

        Invitation invitation = new Invitation(campaign, user);
        this.invitationRepository.save(invitation);

        user.getInvitations().add(invitation);

        this.userRepository.save(user);

        Map<String, Object> infos = new HashMap<>();
        infos.put("email", user.getEmail());
        infos.put("campaignName", campaign.getName());

        this.emailService.sendEmailWithTemplate(user.getEmail(), "invitation_to_join_campaign", infos);

        return user;
    }

    @Transactional
    public String acceptInvitation(String participantEmail, Campaign campaign) throws NotFoundException {
        L.debug("campaignName : {}, participantEmail : {}", campaign.getName(), participantEmail);

        User user = this.getUserByEmail(participantEmail);

        Invitation invitation = this.invitationRepository.findInvitationByUserAndCampaign(user, campaign);

        if (invitation == null) {
            L.error("Invitation not found : {}", participantEmail);

            throw new NotFoundException("Invitation not found.");
        }

        user.getInvitations().remove(invitation);
        user.getCampaignsParticipated().add(campaign);

        this.invitationRepository.delete(invitation);
        this.userRepository.save(user);

        return "Invitation accepted.";
    }

}
