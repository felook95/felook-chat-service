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
import hu.martin.felookchatservice.repository.ConversationRepositoryWithReactiveCrud;
import hu.martin.felookchatservice.repository.MessageRepository;
import hu.martin.felookchatservice.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final ConversationRepositoryWithReactiveCrud withReactiveCrud;

    public ConversationServiceImpl(ConversationRepository conversationRepository,
                                   MessageRepository messageRepository,
                                   UserRepository userRepository,
                                   ConversationRepositoryWithReactiveCrud withReactiveCrud) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.withReactiveCrud = withReactiveCrud;
    }

    @Override
    public Mono<ConversationDto> createConversation(ConversationDto conversationDto) {
        Conversation conversationToSave = new Conversation();
        conversationToSave.setPublicId(UUID.randomUUID());
        conversationToSave.setUsers(new HashSet<>());

        Mono<Conversation> conversationMono = conversationRepository.save(conversationToSave);

        Set<Long> userIds = conversationDto.getUsers().parallelStream()
                .mapToLong(UserDto::getId)
                .boxed()
                .collect(Collectors.toSet());

        Mono<Set<User>> users = userRepository.findAllById(userIds).collect(Collectors.toSet());

        return Mono.zip(conversationMono, users, (conversation, usersSet) -> {
            usersSet.forEach(user -> conversationRepository.addUserToConversation(conversation.getId(), user.getId()));
            usersSet.forEach(user -> conversation.getUsers().add(user));
            return ConversationMapper.toConversationDto(conversation);
        });
    }

    @Override
    public Flux<ConversationDto> getAllConversation() {
        return conversationRepository.findAll()
                .flatMap(conversation -> userRepository.findUsersForConversation(conversation.getId())
                        .collect(Collectors.toSet()).map(conversation::setUsers))
                .flatMap(conversation -> Mono.just(ConversationMapper.toConversationDto(conversation)));
    }

    @Override
    public Mono<ConversationDto> getConversation(Long id) {
        return withReactiveCrud.getConversation(id).map(ConversationMapper::toConversationDto);
    }

    @Override
    public Mono<Void> deleteConversation(Long id) {
        return conversationRepository
                .deleteById(id);
    }

    @Override
    public Mono<MessageDto> addMessage(MessageDto messageDto) {
        Mono<Conversation> conversationMono = conversationRepository.findById(messageDto.getConversation().getId());
        Mono<User> userMono = userRepository.findById(messageDto.getUser().getId());

        return conversationMono.flatMap(conversation -> userMono.flatMap(user -> {
            Message messageToSave = new Message()
                    .setId(null)
                    .setText(messageDto.getText())
                    .setConversationId(conversation.getId())
                    .setUserId(user.getId());

            Mono<Message> messageMono = messageRepository.save(messageToSave);
            return messageMono.map(message -> {
                message
                        .setConversation(conversation)
                        .setUser(user);
                return MessageMapper.toMessageDto(message);
            });
        }));
    }

    @Override
    public Flux<MessageDto> getAllMessageForConversation(Long conversationId) {
        return null;
    }
}
