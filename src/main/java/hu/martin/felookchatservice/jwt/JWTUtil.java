package hu.martin.felookchatservice.jwt;

import com.google.common.base.Strings;
import hu.martin.felookchatservice.auth.ApplicationUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

@Component
public class JWTUtil {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public JWTUtil(JwtConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(Claims claims) {
        final Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    public Boolean isValidRawToken(String rawToken) {
        return !Strings.isNullOrEmpty(rawToken) && rawToken.startsWith(jwtConfig.getTokenPrefix());
    }

    public Mono<String> validateRawToken(String rawToken) {
        return Mono.just(rawToken)
                .map(this::isValidRawToken)
                .flatMap(isValid -> {
                    if (isValid) {
                        return Mono.just(rawToken.replace(jwtConfig.getTokenPrefix(), ""));
                    } else {
                        return Mono.error(new IllegalStateException("Not valid rawToken"));
                    }
                });
    }

    public String generateToken(UserDetails userDetails) {
        String jwtToken = Jwts.builder()
                .claim("authorities", userDetails.getAuthorities())
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(
                        LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())
                ))
                .signWith(secretKey)
                .compact();
        return jwtConfig.getTokenPrefix() + jwtToken;
    }

    public ResponseCookie getCookieForToken(String jwtToken) {
        return ResponseCookie
                .from("jwt", jwtToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(Duration.ofDays(jwtConfig.getTokenExpirationAfterDays()))
                .build();
    }

    public ResponseCookie getInvalidateCookie() {
        return ResponseCookie
                .from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();
    }

}
