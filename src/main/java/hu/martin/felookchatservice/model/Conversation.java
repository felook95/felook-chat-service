package hu.martin.felookchatservice.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Set;
import java.util.UUID;

@Data
@Table("conversation")
@Accessors(chain = true)
public class Conversation {

    @Id
    @Column("id")
    private Long id;

    @Column("public_id")
    private UUID publicId;

    @Transient
    private Set<User> users;
}
