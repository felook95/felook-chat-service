package hu.martin.felookchatservice.service;

import hu.martin.felookchatservice.dto.mapper.ConversationMapper;
import hu.martin.felookchatservice.dto.model.ConversationDto;
import hu.martin.felookchatservice.dto.model.MessageDto;
import hu.martin.felookchatservice.dto.model.UserDto;
import hu.martin.felookchatservice.model.Conversation;
import hu.martin.felookchatservice.model.User;
import hu.martin.felookchatservice.repository.ConversationRepository;
import hu.martin.felookchatservice.repository.MessageRepository;
import hu.martin.felookchatservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ConversationServiceImpl(ConversationRepository conversationRepository,
                                   MessageRepository messageRepository,
                                   UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<ConversationDto> createConversation(ConversationDto conversationDto) {
        Conversation conversationToSave = new Conversation();
        conversationToSave.setPublicId(UUID.randomUUID());
        conversationToSave.setUsers(new HashSet<>());

        Mono<Conversation> conversationMono = conversationRepository.save(conversationToSave);

        var userIds = conversationDto.getUsers().parallelStream()
                .mapToLong(UserDto::getId)
                .boxed()
                .collect(Collectors.toSet());

        Mono<Set<User>> users = userRepository.findAllById(userIds).collect(Collectors.toSet());


        return Mono.zip(conversationMono, users, (conversation, usersSet) -> {
            usersSet.forEach(user -> conversationRepository.addUserToConversation(conversation.getId(), user.getId()));
            usersSet.forEach(user -> conversation.getUsers().add(user));
            return ConversationMapper.toConversationDto(conversationToSave);
        });
    }

    @Override
    public Flux<ConversationDto> getAllConversation() {
        return null;
    }

    @Override
    public Mono<ConversationDto> getConversation(Long id) {
        return null;
    }

    @Override
    public Mono<Void> deleteConversation(Long id) {
        return null;
    }

    @Override
    public Mono<MessageDto> addMessage(MessageDto messageDto) {
        return null;
    }

    @Override
    public Flux<MessageDto> getAllMessageForConversation(Long conversationId) {
        return null;
    }
}
