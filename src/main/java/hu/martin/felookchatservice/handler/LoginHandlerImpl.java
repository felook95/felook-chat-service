package hu.martin.felookchatservice.handler;

import hu.martin.felookchatservice.auth.AuthRequest;
import hu.martin.felookchatservice.jwt.JWTUtil;
import hu.martin.felookchatservice.jwt.JwtConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class LoginHandlerImpl implements LoginHandler {

    private final ReactiveUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private final JwtConfig jwtConfig;

    private final UserDetailsRepositoryReactiveAuthenticationManager authenticationManager;

    public LoginHandlerImpl(@Qualifier("applicationUserServiceImpl") ReactiveUserDetailsService userDetailsService,
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
                                    ResponseCookie responseCookie = jwtUtil.getCookieForToken(jwtToken);
                                    return ServerResponse
                                            .ok()
                                            .cookies(stringResponseCookieMultiValueMap -> {
                                                stringResponseCookieMultiValueMap.add("jwt",
                                                        responseCookie
                                                );
                                            }).build();
                                })
                                .onErrorResume(throwable -> {
                                    ResponseCookie responseCookie = jwtUtil.getInvalidateCookie();
                                    return ServerResponse.status(HttpStatus.UNAUTHORIZED).cookies(stringResponseCookieMultiValueMap -> {
                                        stringResponseCookieMultiValueMap.add("jwt",
                                                responseCookie
                                        );
                                    }).build();
                                }));
    }

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
                            .cookies(stringResponseCookieMultiValueMap -> {
                                stringResponseCookieMultiValueMap.add("jwt",
                                        responseCookie
                                );
                            }).build();
                });
    }
}
