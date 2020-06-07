package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.model.User;
import reactor.core.publisher.Mono;

public interface CustomizedUserRepository {

    Mono<User> getUserById(Long id);
}
