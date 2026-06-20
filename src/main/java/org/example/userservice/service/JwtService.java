package org.example.userservice.service;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.example.userservice.entity.User;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final String secret_key = "mysecretkeymysecretkeymysecretkey12";
    private final long accessTokenValidity = 30 * 60 * 1000;
    private final JwtParser jwtParser;

    public JwtService() {
        this.jwtParser = Jwts.parser().setSigningKey(secret_key);
    }

    public String createToken(User user, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getEmail())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    public String extractEmail(String token) {
        return parseJwtClaims(token).getSubject();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public boolean isTokenExpired(Date expirationDate) throws AuthenticationException {
        return expirationDate.before(new Date());
    }

    public boolean isTokenValid(String accessToken, UserDetails userDetails) {
        String username = userDetails.getUsername();
        Claims claims = parseJwtClaims(accessToken);
        return username.equals(claims.getSubject()) && !isTokenExpired(claims.getExpiration());
    }
}