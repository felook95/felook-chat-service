package hu.martin.felookchatservice.dto.mapper;

import hu.martin.felookchatservice.dto.model.MessageDto;
import hu.martin.felookchatservice.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public static MessageDto toMessageDto(Message message) {
        return new MessageDto()
                .setId(message.getId())
                .setText(message.getText())
                .setConversation(ConversationMapper.toConversationDto(message.getConversation()))
                .setUser(UserMapper.toUserDto(message.getUser()));
    }
}
