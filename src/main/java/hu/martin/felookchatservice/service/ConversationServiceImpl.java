package hu.martin.felookchatservice.service;

import hu.martin.felookchatservice.dto.mapper.ConversationMapper;
import hu.martin.felookchatservice.dto.mapper.MessageMapper;
import hu.martin.felookchatservice.dto.model.ConversationDto;
import hu.martin.felookchatservice.dto.model.MessageDto;
import hu.martin.felookchatservice.dto.model.UserDto;
import hu.martin.felookchatservice.model.Conversation;
import hu.martin.felookchatservice.model.Message;
import hu.martin.felookchatservice.model.User;
import hu.martin.felookchatservice.repository.ConversationRepository;
import hu.martin.felookchatservice.repository.MessageRepository;
import hu.martin.felookchatservice.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;


    public ConversationServiceImpl(ConversationRepository conversationRepository,
                                   MessageRepository messageRepository,
                                   UserRepository userRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<ConversationDto> createConversation(ConversationDto conversationDto) {
        Set<Long> userIds = conversationDto.getUsers().parallelStream()
                .mapToLong(UserDto::getId)
                .boxed()
                .collect(Collectors.toSet());

        return userRepository
                .findAllById(userIds)
                .collect(Collectors.toSet())
                .flatMap(users -> {
                    Conversation conversationToSave = new Conversation();
                    conversationToSave.setPublicId(UUID.randomUUID());
                    conversationToSave.setUsers(users);
                    return conversationRepository.saveConversation(conversationToSave);
                })
                .map(ConversationMapper::toConversationDto);

    }

    @Override
    public Flux<ConversationDto> getAllConversation() {
        return conversationRepository.findAll()
                .flatMap(conversation -> conversationRepository.findUsersForConversation(conversation.getId())
                        .collect(Collectors.toSet()).map(conversation::setUsers))
                .flatMap(conversation -> Mono.just(ConversationMapper.toConversationDto(conversation)));
    }

    @Override
    public Mono<ConversationDto> getConversation(Long id) {
        return conversationRepository.getConversation(id).map(ConversationMapper::toConversationDto);
    }

    @Override
    public Mono<Void> deleteConversation(Long id) {
        return conversationRepository
                .deleteById(id);
    }

    @Override
    public Mono<MessageDto> addMessage(MessageDto messageDto) {
        Mono<Conversation> conversationMono = conversationRepository.getConversation(messageDto.getConversation().getId());
        Mono<User> userMono = userRepository.findById(messageDto.getUser().getId());

        return conversationMono.flatMap(conversation -> userMono.flatMap(user -> {
            Message messageToSave = new Message()
                    .setId(null)
                    .setText(messageDto.getText())
                    .setConversationId(conversation.getId())
                    .setUserId(user.getId())
                    .setConversation(conversation);

            Mono<Message> messageMono = messageRepository.save(messageToSave);
            return messageMono.map(message -> {
                message.setUser(user);
                return MessageMapper.toMessageDto(message);
            });
        }));
    }

    @Override
    public Flux<MessageDto> getAllMessageForConversation(Long conversationId) {
        return conversationRepository.findMessagesByConversationId(conversationId)
                .map(MessageMapper::toMessageDto);
    }
}
