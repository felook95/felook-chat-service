package hu.martin.felookchatservice.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Accessors(chain = true)
@Table("user")
public class User {

    @Id
    @Column("id")
    private Long id;

    @Column("username")
    private String username;

    @Column("password")
    private String password;
}
