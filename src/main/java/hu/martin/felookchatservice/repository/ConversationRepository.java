package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.model.Conversation;
import hu.martin.felookchatservice.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConversationRepository extends ReactiveCrudRepository<Conversation, Long> {

    @Query("insert ignore into sw_conversation_user(conversation_id, user_id) values(:conversationId, :userId)")
    Mono<Void> addUserToConversation(Long conversationId, Long userId);

}
