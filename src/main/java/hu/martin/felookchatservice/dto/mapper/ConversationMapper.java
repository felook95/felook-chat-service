package hu.martin.felookchatservice.dto.mapper;

import hu.martin.felookchatservice.dto.model.ConversationDto;
import hu.martin.felookchatservice.model.Conversation;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ConversationMapper {

    public static ConversationDto toConversationDto(Conversation conversation) {
        return new ConversationDto()
                .setId(conversation.getId())
                .setUsers(conversation
                        .getUsers()
                        .parallelStream()
                        .map(UserMapper::toUserDto)
                        .collect(Collectors.toSet())
                );
    }
}
