package hu.martin.felookchatservice.dto.model;

import hu.martin.felookchatservice.model.Conversation;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class MessageDto {

    private Long id;

    private String text;

    private Conversation conversation;

}
