package hu.martin.felookchatservice.dto.mapper;

import hu.martin.felookchatservice.dto.model.UserProfileDto;
import hu.martin.felookchatservice.model.UserProfile;
import org.springframework.stereotype.Component;

@Component
public class UserProfileMapper {

    public static UserProfileDto toUserProfileDto(UserProfile userProfile) {
        return new UserProfileDto()
                .setId(userProfile.getId())
                .setUserId(userProfile.getUserId())
                .setFirstName(userProfile.getFirstName())
                .setLastName(userProfile.getLastName())
                .setProfileImage(userProfile.getProfileImage());
    }
}
