package fr.pir.configuration.security;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import fr.pir.service.security.CustomUserDetailsService;
import fr.pir.service.security.JwtUtilService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestFilterConfig extends OncePerRequestFilter {

    private static final Logger L = LogManager.getLogger(RequestFilterConfig.class);

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private AuthRequestMatcher authRequestMatcher;

    private String parseJwt(HttpServletRequest request) {
        L.debug("");

        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.replace("Bearer ", "");
        }

        return null;
    }

    public boolean correctJwt(String jwt) {
        L.debug("token : {}", jwt);

        if (jwt == null) {
            return false;
        }

        try {
            String id = this.jwtUtilService.getIdFromToken(jwt);

            UserDetails user = this.customUserDetailsService.loadUserById(id);

            return this.jwtUtilService.validateToken(jwt, user);
        } catch (Exception e) {
            L.error("An exception is raised : {}", e.getMessage());

            return false;
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        L.debug("Processing request: {} {}", method, uri);

        if ("OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);

            return;
        }

        String jwt = parseJwt(request);
        boolean isPublicEndpoint = this.authRequestMatcher.requestMatches(uri) || uri.startsWith("/api/auth");

        if (!isPublicEndpoint) {
            if (jwt != null && correctJwt(jwt)) {
                String id = this.jwtUtilService.getIdFromToken(jwt);
                UserDetails user = this.customUserDetailsService.loadUserById(id);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                        user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("text/plain");
                response.getWriter().write("Invalid or missing bearer token.");
                response.getWriter().flush();
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
