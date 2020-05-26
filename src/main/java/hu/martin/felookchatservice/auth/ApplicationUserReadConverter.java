package hu.martin.felookchatservice.auth;

import hu.martin.felookchatservice.security.ApplicationUserRole;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;

@ReadingConverter
public class ApplicationUserReadConverter implements Converter<Row, ApplicationUser> {
    @Override
    public ApplicationUser convert(@Nonnull Row row) {
        Integer id = row.get("id", Integer.class);
        String username = row.get("username", String.class);
        String password = row.get("password", String.class);
        String role = row.get("role", String.class);
        Boolean isAccountNonExpired = row.get("is_account_non_expired", Boolean.class);
        Boolean isAccountNonLocked = row.get("is_account_non_locked", Boolean.class);
        Boolean isCredentialsNonExpired = row.get("is_credentials_non_expired", Boolean.class);
        Boolean isEnabled = row.get("is_enabled", Boolean.class);

        Assert.notNull(id, "Id must not be null!");
        Assert.notNull(isAccountNonExpired, "isAccountNonExpired must not be null!");
        Assert.notNull(isAccountNonLocked, "isAccountNonLocked must not be null!");
        Assert.notNull(isCredentialsNonExpired, "isCredentialsNonExpired must not be null!");
        Assert.notNull(isEnabled, "isEnabled must not be null!");

        return new ApplicationUser(
                Long.valueOf(id),
                password,
                username,
                role,
                ApplicationUserRole.valueOf(role).getGrantedAuthorities(),
                isAccountNonExpired,
                isAccountNonLocked,
                isCredentialsNonExpired,
                isEnabled

        );
    }
}
