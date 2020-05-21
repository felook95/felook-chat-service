package hu.martin.felookchatservice.auth;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ApplicationUserRepository extends ReactiveCrudRepository<ApplicationUser, Long> {

    Mono<ApplicationUser> findByUsername(String username);
}
