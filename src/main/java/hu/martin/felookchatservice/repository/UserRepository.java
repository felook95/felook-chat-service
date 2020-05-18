package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("select * from user join sw_conversation_user on user.id=sw_conversation_user.user_id where conversation_id=:conversationId")
    Flux<User> findUsersForConversation(Long conversationId);
}
