package hu.martin.felookchatservice.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

public interface ProfileHandler {

    @Nonnull
    Mono<ServerResponse> getProfileByJwtToken(ServerRequest serverRequest);
}
