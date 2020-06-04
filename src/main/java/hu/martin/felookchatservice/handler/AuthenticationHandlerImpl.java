package hu.martin.felookchatservice.handler;

import hu.martin.felookchatservice.auth.ApplicationUser;
import hu.martin.felookchatservice.auth.AuthRequest;
import hu.martin.felookchatservice.auth.AuthenticationCookieAndResponseProvider;
import hu.martin.felookchatservice.jwt.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Component
@Log4j2
public class AuthenticationHandlerImpl implements AuthenticationHandler {

    private final JWTUtil jwtUtil;
    private final AuthenticationCookieAndResponseProvider authenticationCookieAndResponseProvider;

    private final UserDetailsRepositoryReactiveAuthenticationManager authenticationManager;

    public AuthenticationHandlerImpl(
            JWTUtil jwtUtil,
            AuthenticationCookieAndResponseProvider authenticationCookieAndResponseProvider,
            UserDetailsRepositoryReactiveAuthenticationManager authenticationManager
    ) {
        this.jwtUtil = jwtUtil;
        this.authenticationCookieAndResponseProvider = authenticationCookieAndResponseProvider;
        this.authenticationManager = authenticationManager;
    }

    @Nonnull
    @Override
    public Mono<ServerResponse> loginApplicationUser(ServerRequest request) {
        return request.bodyToMono(AuthRequest.class)
                .flatMap(authRequest ->
                {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    authRequest.getUsername(),
                                    authRequest.getPassword()
                            );
                    return authenticationManager.authenticate(usernamePasswordAuthenticationToken)
                            .flatMap(authentication ->
                                    authenticationCookieAndResponseProvider
                                            .getLoginResponse((ApplicationUser) authentication.getPrincipal()))
                            .onErrorResume(throwable -> {
                                ResponseCookie jwtInvalidatingCookie = jwtUtil.getInvalidateCookie();
                                return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                        .cookie(authenticationCookieAndResponseProvider.loggedOutCookie())
                                        .cookie(jwtInvalidatingCookie).build();
                            });
                });
    }

    @Nonnull
    @Override
    public Mono<ServerResponse> logoutApplicationUser(ServerRequest request) {
        ResponseCookie jwtInvalidatingCookie = jwtUtil.getInvalidateCookie();
        return ServerResponse.ok()
                .cookie(authenticationCookieAndResponseProvider.loggedOutCookie())
                .cookie(jwtInvalidatingCookie).build();
    }

}
