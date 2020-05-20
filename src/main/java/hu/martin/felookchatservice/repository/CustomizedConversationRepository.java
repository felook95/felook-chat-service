package hu.martin.felookchatservice.repository;

import hu.martin.felookchatservice.model.Conversation;
import hu.martin.felookchatservice.model.Message;
import hu.martin.felookchatservice.model.User;
import hu.martin.felookchatservice.model.helper.SwitchConversationUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomizedConversationRepository {

    Mono<Conversation> saveConversation(Conversation conversation);

    Mono<Conversation> getConversation(Long id);

    Flux<Message> findMessagesByConversationId(Long conversationId);

    Flux<User> findUsersForConversation(Long conversationId);

    Mono<SwitchConversationUser> addUserToConversation(Long conversationId, Long userId);
}
