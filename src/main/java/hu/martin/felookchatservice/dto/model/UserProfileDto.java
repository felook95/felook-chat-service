package hu.martin.felookchatservice.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Accessors(chain = true)
@JsonInclude(NON_NULL)
public class UserProfileDto {

    private Long id;

    private Long userId;

    private String firstName;

    private String lastName;

    private String profileImage;
}
