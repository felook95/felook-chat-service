package hu.martin.felookchatservice.service;

import hu.martin.felookchatservice.dto.model.ConversationDto;
import hu.martin.felookchatservice.dto.model.MessageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ConversationService {

    Mono<ConversationDto> createConversation(ConversationDto conversationDto);

    Flux<ConversationDto> getAllConversation();

    Mono<ConversationDto> getConversation(Long id);

    Mono<Void> deleteConversation(Long id);

    Mono<MessageDto> addMessage(MessageDto messageDto);

    Flux<MessageDto> getAllMessageForConversation(Long conversationId);
}
