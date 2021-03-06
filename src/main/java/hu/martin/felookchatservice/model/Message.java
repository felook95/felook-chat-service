package hu.martin.felookchatservice.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("message")
@Accessors(chain = true)
public class Message {

    @Id
    @Column("id")
    private Long id;

    @Column("text")
    private String text;

    @Column("user_id")
    private Long userId;

    @Column("conversation_id")
    private Long conversationId;

    @Transient
    private User user;

    @Transient
    private Conversation conversation;

}
