package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.model.User;
import hu.martin.felookchatservice.model.UserProfile;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {

    private final DatabaseClient databaseClient;

    public CustomizedUserRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<User> getUserById(Long id) {
        Mono<User> userMono = databaseClient.execute("select * from user where user.id=:userId")
                .bind("userId", id)
                .as(User.class)
                .fetch()
                .first();

        Mono<UserProfile> userProfileMono = databaseClient.execute("select * from user_profile where user_id=:userId")
                .bind("userId", id)
                .as(UserProfile.class)
                .fetch()
                .first();

        return Mono.zip(userMono, userProfileMono, User::setUserProfile);
    }
}
