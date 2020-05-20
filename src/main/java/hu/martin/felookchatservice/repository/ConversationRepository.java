package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.model.Conversation;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ConversationRepository extends ReactiveCrudRepository<Conversation, Long>, CustomizedConversationRepository {
}
