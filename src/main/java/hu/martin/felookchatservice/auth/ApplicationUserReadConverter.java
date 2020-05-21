package hu.martin.felookchatservice.auth;

import hu.martin.felookchatservice.security.ApplicationUserRole;
import io.r2dbc.spi.Row;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import javax.annotation.Nonnull;

@ReadingConverter
public class ApplicationUserReadConverter implements Converter<Row, ApplicationUser> {
    @Override
    public ApplicationUser convert(@Nonnull Row row) {
        String role = row.get("role", String.class);
        ApplicationUser applicationUser = new ApplicationUser(
                row.get("id", Long.class),
                row.get("password", String.class),
                row.get("username", String.class),
                role,
                ApplicationUserRole.valueOf(role).getGrantedAuthorities(),
                row.get("is_account_non_expired", boolean.class),
                row.get("is_account_non_locked", boolean.class),
                row.get("is_credentials_non_expired", boolean.class),
                row.get("is_enabled", boolean.class)

        );
        return applicationUser;
    }
}
