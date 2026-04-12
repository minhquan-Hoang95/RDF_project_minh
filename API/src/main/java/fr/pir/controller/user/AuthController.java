package fr.pir.controller.user;

import javax.security.auth.login.AccountException;

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

import fr.pir.exception.EmailException;
import fr.pir.exception.NotFoundException;
import fr.pir.exception.VerificationCodeException;
import fr.pir.model.user.User;
import fr.pir.service.user.AuthService;
import fr.pir.service.user.UserService;

import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger L = LogManager.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @GetMapping("/validate")
    public ResponseEntity<String> validateAccount(@RequestParam String email, @RequestParam String code) {
        L.debug("email : {}", email);

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.authService.validateAccount(email, code));
        } catch (NotFoundException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (VerificationCodeException | AccountException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            L.error("An exception is raised", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    @GetMapping
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password,
            HttpServletRequest request) {
        L.debug("email : {}", email);

        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.authService.login(email, password, request));
        } catch (NotFoundException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadCredentialsException | AccountException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (LockedException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account locked.");
        } catch (Exception e) {
            L.error("An exception is raised", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    @PatchMapping
    public ResponseEntity<String> passwordForgotten(@RequestParam String email) {
        L.debug("email : {}", email);

        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.authService.passwordForgotten(email));
        } catch (NotFoundException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    @PutMapping
    public ResponseEntity<String> newPassword(@RequestParam String email, @RequestParam String code,
            @RequestParam String password, HttpServletRequest request) {
        L.debug("email : {}", email);

        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(this.authService.newPassword(email, code, password, request));
        } catch (NotFoundException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (VerificationCodeException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            L.error("An exception is raised", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

    @PostMapping
    public ResponseEntity<String> signup(@RequestBody User user) {
        L.debug("email : {}", user.getEmail());

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.signup(user));
        } catch (EmailException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (MailSendException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Not possible to send email to this email address.");
        } catch (MailAuthenticationException e) {
            L.error("An exception is raised : {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Mail dispatch service currently offline.");
        } catch (DataIntegrityViolationException e) {
            L.error("An exception is raised : {}", e.getMessage());

            String error = e.getMessage();

            if (error.contains("duplicate key")) {
                if (error.contains("Key (email)=")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate key email.");
                }
            }

            return ResponseEntity.status(HttpStatus.CONFLICT).body("Data integrity violation.");
        } catch (Exception e) {
            L.error("An exception is raised", e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Oops, error. Please contact an administrator.");
        }
    }

}
