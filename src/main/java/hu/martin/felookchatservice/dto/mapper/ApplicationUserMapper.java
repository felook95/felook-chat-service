package hu.martin.felookchatservice.dto.mapper;

import hu.martin.felookchatservice.auth.ApplicationUser;
import hu.martin.felookchatservice.dto.model.ApplicationUserDto;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUserMapper {
    public static ApplicationUserDto toApplicationUserDto(ApplicationUser applicationUser) {
        return new ApplicationUserDto()
                .setId(applicationUser.getId())
                .setUsername(applicationUser.getUsername())
                .setRole(applicationUser.getRole())
                .setEnabled(applicationUser.isEnabled());
    }

}
