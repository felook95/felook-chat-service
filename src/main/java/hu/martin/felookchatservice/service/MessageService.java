package hu.martin.felookchatservice.service;

import hu.martin.felookchatservice.dto.model.MessageDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MessageService {

    Flux<MessageDto> getMessages();

    Mono<MessageDto> getMessageById(Long id);

    Mono<MessageDto> createMessage(MessageDto messageDto);

    Mono<Void> deleteMessage(Long id);

}
