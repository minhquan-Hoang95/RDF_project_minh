package fr.pir.configuration.security;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthenticationEntryPointConfig implements AuthenticationEntryPoint, Serializable {

    private static final Logger L = LogManager.getLogger(AuthenticationEntryPointConfig.class);

    private static final long serialVersionUID = 333L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        L.debug("");

        if (response.getStatus() == 403 && response.getContentType() == null) {
            response.setContentType("text/plain");
            response.setContentLength("Permission denied. Disappear !".length());
            response.getWriter().write("Permission denied. Disappear !");
            response.getWriter().flush();
        }
    }

}
