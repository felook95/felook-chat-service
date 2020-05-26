package hu.martin.felookchatservice.auth;

import reactor.core.publisher.Mono;

public interface CustomizedApplicationUserRepository {

    Mono<Boolean> existsByUsername(String username);
}
