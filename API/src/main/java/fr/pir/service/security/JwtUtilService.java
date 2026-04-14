package fr.pir.service.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JwtUtilService {

    private static final Logger L = LogManager.getLogger(JwtUtilService.class);

    private final SecretKey secretKey;

    @Autowired
    public JwtUtilService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public String getIdFromToken(String token) {
        L.debug("token : {}", token);

        return this.getClaimFromToken(token, Claims::getSubject);
    }

    public String getIdFromAuthorization(String authorization) {
        L.debug("authorization : {}", authorization);

        String token = authorization.replace("Bearer ", "");

        return this.getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        L.debug("token : {}", token);

        return this.getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        L.debug("token : {}", token);

        Claims claims = this.getAllClaimsFromToken(token);

        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        L.debug("token : {}", token);

        return Jwts.parser().verifyWith(this.secretKey).build().parseSignedClaims(token).getPayload();
    }

    private Boolean isTokenExpired(String token) {
        L.debug("token : {}", token);

        Date expiration = this.getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    public String generateToken(String userId) {
        L.debug("userId : {}", userId);

        Map<String, Object> claims = new HashMap<>();

        return this.doGenerateToken(claims, userId);
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        L.debug("subject : {}", subject);

        return Jwts.builder().claims(claims).subject(subject).issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + this.jwtExpiration * 1000)).signWith(this.secretKey)
                .compact();
    }

    public Boolean validateToken(String token, UserDetails user) {
        L.debug("token : {}", token);

        try {
            String id = this.getIdFromToken(token);
            return (id.equals(user.getUsername()) && !this.isTokenExpired(token));
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return false;
        }
    }

}
