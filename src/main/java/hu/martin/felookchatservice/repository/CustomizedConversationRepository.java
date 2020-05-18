package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.model.Conversation;
import hu.martin.felookchatservice.model.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomizedConversationRepository {
    Mono<Conversation> getConversation(Long id);

    Flux<Message> findMessagesByConversationId(Long conversationId);
}
