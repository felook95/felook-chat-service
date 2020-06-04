package hu.martin.felookchatservice.auth;

import hu.martin.felookchatservice.dto.mapper.ApplicationUserMapper;
import hu.martin.felookchatservice.dto.model.ApplicationUserDto;
import hu.martin.felookchatservice.jwt.JWTUtil;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class AuthenticationCookieAndResponseProvider {

    private final JWTUtil jwtUtil;

    public AuthenticationCookieAndResponseProvider(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Mono<ServerResponse> getLoginResponse(ApplicationUser applicationUser) {
        return Mono.just(applicationUser)
                .flatMap(applicationUser1 -> {
                    String jwtToken = jwtUtil.generateToken(applicationUser1);
                    ResponseCookie jwtCookie = jwtUtil.getCookieForToken(jwtToken);

                    Mono<ApplicationUserDto> applicationUserDtoMono =
                            Mono.just(ApplicationUserMapper.toApplicationUserDto(applicationUser1));

                    return ServerResponse.ok()
                            .cookie(jwtCookie)
                            .cookie(loggedInCookie(jwtCookie.getMaxAge()))
                            .body(applicationUserDtoMono, ApplicationUserDto.class);
                });
    }

    public ResponseCookie loggedInCookie(Duration maxAge) {
        return ResponseCookie
                .from("is-logged-in", "true")
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    public ResponseCookie loggedOutCookie() {
        return ResponseCookie
                .from("is-logged-in", "false")
                .path("/")
                .maxAge(0)
                .build();
    }
}
