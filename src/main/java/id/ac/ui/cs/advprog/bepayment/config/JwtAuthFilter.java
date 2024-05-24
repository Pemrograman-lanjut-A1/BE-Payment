package id.ac.ui.cs.advprog.bepayment.config;

import id.ac.ui.cs.advprog.bepayment.service.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
@Component
public class JwtAuthFilter {

    private final JwtService jwtService;

    public JwtAuthFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    public String filterToken(String token) {
        String accessToken = jwtService.resolveToken(token);
        if (accessToken == null) {
            return null;
        }
        Claims claims = jwtService.resolveClaims(token);
        if (claims != null && jwtService.validateClaims(claims)) {
            return claims.get("Role").toString();
        }
        return null;
    }
}
