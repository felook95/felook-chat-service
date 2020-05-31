package hu.martin.felookchatservice.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

public interface AuthenticationHandler {

    @Nonnull
    Mono<ServerResponse> loginApplicationUser(ServerRequest request);

    @Nonnull
    Mono<ServerResponse> logoutApplicationUser(ServerRequest request);

    @Nonnull
    Mono<ServerResponse> getJwtToken(ServerRequest serverRequest);
}
