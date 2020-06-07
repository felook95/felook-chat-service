package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.auth.ApplicationUser;
import reactor.core.publisher.Mono;

public interface CustomizedApplicationUserRepository {

    Mono<Boolean> existsByUsername(String username);

    Mono<ApplicationUser> findByUsername(String username);
}
