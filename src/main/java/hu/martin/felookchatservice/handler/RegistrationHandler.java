package hu.martin.felookchatservice.handler;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

public interface RegistrationHandler {

    @Nonnull
    Mono<ServerResponse> registerApplicationUser(ServerRequest request);
}
