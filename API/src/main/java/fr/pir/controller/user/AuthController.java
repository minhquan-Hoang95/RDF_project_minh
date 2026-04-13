package fr.pir.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.dto.LoginRequest;
import fr.pir.exception.EmailException;
import fr.pir.exception.NotFoundException;
import fr.pir.exception.VerificationCodeException;
import fr.pir.model.user.User;
import fr.pir.service.user.AuthService;
import fr.pir.service.user.UserService;

/**
 * Authentication controller handling login, signup, password reset
 */
@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger L = LogManager.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    /**
     * Validate account with email verification code
     */
    @GetMapping("/validate")
    public ResponseEntity<String> validateAccount(@RequestParam String email, @RequestParam String code) {
        L.debug("Validating account");

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.authService.validateAccount(email, code));
        } catch (NotFoundException e) {
            L.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (VerificationCodeException | fr.pir.exception.AccountException e) {
            L.error("Verification error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            L.error("Validation error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    /**
     * Login endpoint - NEW: Uses POST with body instead of GET with params
     * @param loginRequest - { "email": "user@example.com", "password": "password123" }
     * @param request - HTTP request context
     * @return JWT token and user information
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {

        L.debug("Login attempt");

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.authService.login(loginRequest.getEmail(),
                                                loginRequest.getPassword(),
                                                request));
        } catch (NotFoundException e) {
            L.error("User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (BadCredentialsException e) {
            L.warn("Invalid credentials attempt");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid email or password");
        } catch (LockedException e) {
            L.warn("Account locked");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account is locked");
        } catch (Exception e) {
            L.error("Login error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login");
        }
    }

    /**
     * DEPRECATED: Use POST /auth/login instead
     * @deprecated
     */
    @GetMapping
    @Deprecated(since = "2.1.0", forRemoval = true)
    public ResponseEntity<String> loginDeprecated(@RequestParam String email,
                                             @RequestParam String password,
                                             HttpServletRequest request) {
        L.warn("Deprecated GET /api/auth endpoint used. Please use POST /api/auth/login instead");
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).build();
    }

    /**
     * Request password reset - sends verification code to email
     */
    @PatchMapping
    public ResponseEntity<String> passwordForgotten(@RequestParam String email) {
        L.debug("Password reset requested");

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.userService.passwordForgotten(email));
        } catch (NotFoundException e) {
            L.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (MailSendException e) {
            L.error("Email send error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Not possible to send email to this address.");
        } catch (Exception e) {
            L.error("Password reset error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during password reset");
        }
    }

    /**
     * Set new password after verification
     */
    @PutMapping
    public ResponseEntity<String> newPassword(@RequestParam String email,
                                         @RequestParam String code,
                                         @RequestParam String password,
                                         HttpServletRequest request) {
        L.debug("Setting new password");

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.userService.newPassword(email, code, password, request));
        } catch (NotFoundException e) {
            L.error("User not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (VerificationCodeException e) {
            L.error("Invalid verification code: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            L.error("Password update error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating password");
        }
    }

    /**
     * Sign up new user - requires validation
     * @param user - User data with @Valid validation
     * @return Confirmation message
     */
    @PostMapping
    public ResponseEntity<String> signup(@Valid @RequestBody User user) {
        L.debug("Signup request");

        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(this.userService.signup(user));
        } catch (EmailException | IllegalArgumentException e) {
            L.error("Email error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalStateException e) {
            L.error("System configuration error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (MailSendException e) {
            L.error("Email send error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Not possible to send email to this address.");
        } catch (MailAuthenticationException e) {
            L.error("Mail service error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Mail service is currently offline.");
        } catch (DataIntegrityViolationException e) {
            L.error("Data integrity error: {}", e.getMessage());
            String error = e.getMessage();
            if (error != null && error.contains("duplicate key")) {
                if (error.contains("Key (email)=")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Email already registered.");
                }
            }
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Data integrity violation.");
        } catch (Exception e) {
            L.error("Signup error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

}
