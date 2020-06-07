package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, Long>, CustomizedUserRepository {
}
