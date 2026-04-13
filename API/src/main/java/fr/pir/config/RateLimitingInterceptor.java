package fr.pir.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate limiting interceptor using Bucket4j
 * Prevents brute force attacks on authentication endpoints
 */
@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private static final Logger L = LogManager.getLogger(RateLimitingInterceptor.class);

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    // Login: 5 requests per 15 minutes
    private static final Bandwidth LOGIN_LIMIT =
        Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(15)));

    // Signup: 3 requests per hour
    private static final Bandwidth SIGNUP_LIMIT =
        Bandwidth.classic(3, Refill.intervally(3, Duration.ofHours(1)));

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler) throws Exception {
        String path = request.getRequestURI();
        String ip = getClientIP(request);

        Bucket bucket = null;

        if (path.contains("/auth/login")) {
            bucket = resolveBucket(ip + ":login", LOGIN_LIMIT);
        } else if (path.contains("/auth") && request.getMethod().equals("POST")
                   && !path.contains("/login")) {
            bucket = resolveBucket(ip + ":signup", SIGNUP_LIMIT);
        }

        if (bucket != null && !bucket.tryConsume(1)) {
            L.warn("Rate limit exceeded for IP: {}", ip);
            response.setStatus(429);
            response.setContentType("application/json");

            JSONObject errorResponse = new JSONObject();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Too many requests. Please try again later.");
            errorResponse.put("code", 429);

            response.getWriter().write(errorResponse.toString());
            return false;
        }

        return true;
    }

    private Bucket resolveBucket(String key, Bandwidth limit) {
        return cache.computeIfAbsent(key, k -> Bucket4j.builder()
            .addLimit(limit)
            .build());
    }

    /**
     * Extract client IP address from request
     * Checks X-Forwarded-For header for proxied requests
     */
    private String getClientIP(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}

