package hu.martin.felookchatservice.dto.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class ConversationDto {

    private Long id;

    private Set<UserDto> users;
}
