package hu.martin.felookchatservice.service;

import hu.martin.felookchatservice.dto.mapper.MessageMapper;
import hu.martin.felookchatservice.dto.model.MessageDto;
import hu.martin.felookchatservice.model.Message;
import hu.martin.felookchatservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Flux<MessageDto> getMessages() {
        return messageRepository.findAll()
                .map(MessageMapper::toMessageDto);
    }

//    public Mono<ServerResponse> getMessageByIdd(Long id) {
//        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
//    }

    @Override
    public Mono<MessageDto> getMessageById(Long id) {
        return messageRepository.findById(id)
                .map(MessageMapper::toMessageDto);
    }

    @Override
    public Mono<MessageDto> createMessage(MessageDto messageDto) {
        Message message = new Message()
                .setText(messageDto.getText());

        Mono<Message> messageMono = messageRepository.save(message);

        return messageMono.map(MessageMapper::toMessageDto);
    }

    @Override
    public Mono<Void> deleteMessage(Long id) {
        return messageRepository.deleteById(id);
    }

}
