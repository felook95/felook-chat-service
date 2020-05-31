package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.auth.ApplicationUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ApplicationUserRepository extends ReactiveCrudRepository<ApplicationUser, Long>, CustomizedApplicationUserRepository {

    Mono<ApplicationUser> findByUsername(String username);
}
