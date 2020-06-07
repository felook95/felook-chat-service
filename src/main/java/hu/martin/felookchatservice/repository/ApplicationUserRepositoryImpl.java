package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.auth.ApplicationUser;
import hu.martin.felookchatservice.model.User;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ApplicationUserRepositoryImpl implements CustomizedApplicationUserRepository {

    private final DatabaseClient databaseClient;
    private final UserRepository userRepository;

    public ApplicationUserRepositoryImpl(DatabaseClient databaseClient, UserRepository userRepository) {
        this.databaseClient = databaseClient;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Boolean> existsByUsername(String username) {
        return databaseClient.execute("select * from application_user where username=:username")
                .bind("username", username)
                .map((row, rowMetadata) -> row)
                .first().hasElement();
    }

    @Override
    public Mono<ApplicationUser> findByUsername(String username) {
        return databaseClient.execute("select * from application_user where username=:username")
                .as(ApplicationUser.class)
                .bind("username", username)
                .fetch()
                .first()
                .flatMap(applicationUser -> userRepository
                        .getUserById(applicationUser.getUserId())
                        .map(applicationUser::setUser));
    }
}
