package hu.martin.felookchatservice.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

public interface LoginHandler {

    @Nonnull
    Mono<ServerResponse> loginApplicationUser(ServerRequest request);

    Mono<ServerResponse> getJwtToken(ServerRequest serverRequest);
}
