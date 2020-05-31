package hu.martin.felookchatservice.repository;

import reactor.core.publisher.Mono;

public interface CustomizedApplicationUserRepository {

    Mono<Boolean> existsByUsername(String username);
}
