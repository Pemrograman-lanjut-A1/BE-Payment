package id.ac.ui.cs.advprog.bepayment.config;

import id.ac.ui.cs.advprog.bepayment.service.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFilterToken_Success() {
        String token = "valid-token";
        String accessToken = "access-token";
        Claims claims = mock(Claims.class);

        when(jwtService.resolveToken(token)).thenReturn(accessToken);
        when(jwtService.resolveClaims(token)).thenReturn(claims);
        when(jwtService.validateClaims(claims)).thenReturn(true);
        when(claims.get("Role")).thenReturn("USER_ROLE");

        String result = jwtAuthFilter.filterToken(token);

        assertEquals("USER_ROLE", result);
        verify(jwtService).resolveToken(token);
        verify(jwtService).resolveClaims(token);
        verify(jwtService).validateClaims(claims);
    }

    @Test
    void testFilterToken_TokenNotResolved() {
        String token = "invalid-token";

        when(jwtService.resolveToken(token)).thenReturn(null);

        String result = jwtAuthFilter.filterToken(token);

        assertNull(result);
        verify(jwtService).resolveToken(token);
        verify(jwtService, never()).resolveClaims(token);
        verify(jwtService, never()).validateClaims(any());
    }

    @Test
    void testFilterToken_InvalidClaims() {
        String token = "valid-token";
        String accessToken = "access-token";
        Claims claims = mock(Claims.class);

        when(jwtService.resolveToken(token)).thenReturn(accessToken);
        when(jwtService.resolveClaims(token)).thenReturn(claims);
        when(jwtService.validateClaims(claims)).thenReturn(false);

        String result = jwtAuthFilter.filterToken(token);

        assertNull(result);
        verify(jwtService).resolveToken(token);
        verify(jwtService).resolveClaims(token);
        verify(jwtService).validateClaims(claims);
        verify(claims, never()).get("Role");
    }

    @Test
    void testFilterToken_NullClaims() {
        String token = "valid-token";
        String accessToken = "access-token";

        when(jwtService.resolveToken(token)).thenReturn(accessToken);
        when(jwtService.resolveClaims(token)).thenReturn(null);

        String result = jwtAuthFilter.filterToken(token);

        assertNull(result);
        verify(jwtService).resolveToken(token);
        verify(jwtService).resolveClaims(token);
        verify(jwtService, never()).validateClaims(any());
    }
}
