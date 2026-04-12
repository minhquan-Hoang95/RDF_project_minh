package fr.pir.configuration.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
public class AuthRequestMatcher {

    @Value("${uri.authorized}")
    private List<String> uriAuthorized;

    private static final Logger L = LogManager.getLogger(AuthRequestMatcher.class);

    public RequestMatcher getRequestMatchers() {
        L.debug("");

        RequestMatcher requestMatcher = (request) -> this.requestMatches(request.getRequestURI());

        return requestMatcher;
    }

    public boolean requestMatches(String uri) {
        L.debug("uri : {}", uri);

        for (String testUri : this.uriAuthorized) {
            if (uri.contains(testUri)) {
                return true;
            }
        }

        return false;
    }

}
