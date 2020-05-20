package hu.martin.felookchatservice.model.helper;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("sw_conversation_user")
@Accessors(chain = true)
public class SwitchConversationUser {

    @Id
    @Column("id")
    private Long id;

    @Column("conversation_id")
    private Long conversationId;

    @Column("user_id")
    private Long userId;
}

