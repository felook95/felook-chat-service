package hu.martin.felookchatservice.dto.model;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageDto {

    private Long id;

    private String text;

    private UserDto user;

    private ConversationDto conversation;

}
