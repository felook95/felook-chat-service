package hu.martin.felookchatservice.dto.mapper;

import hu.martin.felookchatservice.dto.model.ConversationDto;
import hu.martin.felookchatservice.dto.model.UserDto;
import hu.martin.felookchatservice.model.Conversation;
import hu.martin.felookchatservice.model.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ConversationMapper {

    public static ConversationDto toConversationDto(Conversation conversation) {
        return new ConversationDto()
                .setId(conversation.getId())
                .setUsers(mapConversationUsers(conversation.getUsers())
                );
    }

    private static Set<UserDto> mapConversationUsers(Set<User> users) {
        return Optional.ofNullable(users)
                .orElseGet(HashSet::new)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toSet());

    }
}
