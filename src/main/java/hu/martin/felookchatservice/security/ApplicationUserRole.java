package hu.martin.felookchatservice.security;

import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static hu.martin.felookchatservice.security.ApplicationUserPermission.*;

@Getter
@AllArgsConstructor
public enum ApplicationUserRole {
    USER(Sets.newHashSet(
            MESSAGE_READ,
            MESSAGE_WRITE,
            USER_READ
    )),
    ADMIN(Sets.newHashSet(
            MESSAGE_READ,
            MESSAGE_WRITE,
            USER_READ,
            USER_WRITE
    ));

    private final Set<ApplicationUserPermission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(applicationUserPermission -> new SimpleGrantedAuthority(applicationUserPermission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

}
