package id.ac.ui.cs.advprog.bepayment.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

import io.jsonwebtoken.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Component
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey = "6o0fY3XZm6vcwmuOalTRZvMZmJ31DO2NyOSjJoj4XRwz7uGI8FAQ5kELHS+pmAD+i9idb7Sg8uigefSVAfwBXA==";

    private final JwtParser jwtParser;

    private static final String BEARERPREFIX = "Bearer ";

    public JwtService(){
        this.jwtParser = Jwts.parserBuilder()
                .setSigningKey(Base64.getDecoder().decode(secretKey))
                .build();
    }
    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(String bearerToken) {
        String token = resolveToken(bearerToken);
        if (token != null) {
            return parseJwtClaims(token);
        }
        return null;
    }

    public String resolveToken(String bearerToken) {

        if (bearerToken != null && bearerToken.startsWith(BEARERPREFIX)) {
            return bearerToken.substring(BEARERPREFIX.length());
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }
}
