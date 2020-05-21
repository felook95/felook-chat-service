package hu.martin.felookchatservice.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplicationUserPermission {
    MESSAGE_READ("message:read"),
    MESSAGE_WRITE("message:write"),
    USER_READ("user:read"),
    USER_WRITE("user:write");

    private final String permission;

}
