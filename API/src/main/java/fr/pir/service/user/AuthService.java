package fr.pir.service.user;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.pir.model.user.User;
import fr.pir.service.security.JwtUtilService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    private static final Logger L = LogManager.getLogger(AuthService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilService jwtUtilService;

    private static final String EMAIL_REGEX = "^[\\w\\-\\.]+@([\\w\\-]+\\.)+[\\w\\-]{2,4}$";

    public String login(String email, String password, HttpServletRequest request) throws Exception {
        L.debug("email : {}", email);

        if (email == null || !email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters");
        }

        User user = this.userService.getUserByEmail(email);

        this.authenticate(email, password);

        String token = this.jwtUtilService.generateToken(String.valueOf(user.getId()));

        JSONObject response = new JSONObject();

        response.put("id", user.getId());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());
        response.put("role", user.getRole().getName());
        response.put("token", token);

        return response.toString();
    }

    private boolean authenticate(String email, String password) throws Exception {
        L.debug("email : {}", email);

        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        return true;
    }

    public String validateAccount(String email, String code) throws Exception {
        L.debug("email : {}", email);

        return this.userService.validateAccount(email, code);
    }

    public String authentication(String email, String password, HttpServletRequest request)
            throws Exception {
        L.debug("email : {}", email);

        return this.login(email, password, request);
    }

    public String passwordForgotten(String email) throws Exception {
        L.debug("email : {}", email);

        return this.userService.passwordForgotten(email);
    }

    public String newPassword(String email, String code, String password, HttpServletRequest request) throws Exception {
        L.debug("email : {}", email);

        return this.userService.newPassword(email, code, password, request);
    }

}
