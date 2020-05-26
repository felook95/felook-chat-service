package hu.martin.felookchatservice.auth;

import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ApplicationUserRepositoryImpl implements CustomizedApplicationUserRepository {

    private final DatabaseClient databaseClient;

    public ApplicationUserRepositoryImpl(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Boolean> existsByUsername(String username) {
        // TODO: 2020. 05. 26. write sql
        return databaseClient.execute("").map((row, rowMetadata) -> row).first().hasElement();
    }
}
