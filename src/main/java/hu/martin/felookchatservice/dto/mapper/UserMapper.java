package hu.martin.felookchatservice.dto.mapper;

import hu.martin.felookchatservice.dto.model.UserDto;
import hu.martin.felookchatservice.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto()
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setUserProfile(UserProfileMapper.toUserProfileDto(user.getUserProfile()));
    }
}
