package hu.martin.felookchatservice.handler;

import hu.martin.felookchatservice.auth.AuthRequest;
import hu.martin.felookchatservice.jwt.JWTUtil;
import hu.martin.felookchatservice.jwt.JwtConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.time.Duration;

@Component
@Log4j2
public class AuthenticationHandlerImpl implements AuthenticationHandler {

    private final ReactiveUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private final JwtConfig jwtConfig;

    private final UserDetailsRepositoryReactiveAuthenticationManager authenticationManager;

    public AuthenticationHandlerImpl(@Qualifier("applicationUserServiceImpl") ReactiveUserDetailsService userDetailsService,
                                     JWTUtil jwtUtil, JwtConfig jwtConfig, UserDetailsRepositoryReactiveAuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.jwtConfig = jwtConfig;
        this.authenticationManager = authenticationManager;
    }

    @Nonnull
    @Override
    public Mono<ServerResponse> loginApplicationUser(ServerRequest request) {
        return request.bodyToMono(AuthRequest.class)
                .flatMap(authRequest ->
                        authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(authRequest.getUsername(),
                                        authRequest.getPassword()
                                )
                        )
                                .flatMap(authentication -> {
                                    String jwtToken = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
                                    ResponseCookie jwtCookie = jwtUtil.getCookieForToken(jwtToken);
                                    return ServerResponse
                                            .ok()
                                            .cookie(loggedInCookie())
                                            .cookie(jwtCookie).build();
                                })
                                .onErrorResume(throwable -> {
                                    ResponseCookie jwtInvalidatingCookie = jwtUtil.getInvalidateCookie();
                                    return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                            .cookie(loggedOutCookie())
                                            .cookie(jwtInvalidatingCookie).build();
                                }));
    }

    @Nonnull
    @Override
    public Mono<ServerResponse> logoutApplicationUser(ServerRequest request) {
        ResponseCookie jwtInvalidatingCookie = jwtUtil.getInvalidateCookie();
        return ServerResponse.ok()
                .cookie(loggedOutCookie())
                .cookie(jwtInvalidatingCookie).build();
    }

    @Nonnull
    @Override
    public Mono<ServerResponse> getJwtToken(ServerRequest serverRequest) {
        return userDetailsService
                .findByUsername("felook")
                .flatMap(userDetails -> {
                    String jwtToken = jwtUtil.generateToken(userDetails);
                    ResponseCookie responseCookie = ResponseCookie
                            .from("jwt", jwtToken)
                            .httpOnly(true)
                            .secure(false)
                            .path("/")
                            .maxAge(Duration.ofDays(jwtConfig.getTokenExpirationAfterDays()))
                            .build();
                    return ServerResponse
                            .ok()
                            .cookie(responseCookie).build();
                });
    }

    @Nonnull
    private ResponseCookie loggedInCookie() {
        return ResponseCookie
                .from("is-logged-in", "true")
                .path("/")
                .maxAge(Duration.ofDays(jwtConfig.getTokenExpirationAfterDays()))
                .build();
    }

    @Nonnull
    private ResponseCookie loggedOutCookie() {
        return ResponseCookie
                .from("is-logged-in", "false")
                .path("/")
                .maxAge(Duration.ofDays(jwtConfig.getTokenExpirationAfterDays()))
                .build();
    }
}
