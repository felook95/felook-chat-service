package hu.martin.felookchatservice.auth;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@AllArgsConstructor
@Table("application_user")
public class ApplicationUser implements UserDetails {

    @Id
    @Column("id")
    private final Long id;

    @Column("password")
    private final String password;

    @Column("username")
    private final String username;

    @Column("role")
    private final String role;

    @Transient
    private final Set<? extends GrantedAuthority> grantedAuthorities;

    @Column("is_account_non_expired")
    private final boolean isAccountNonExpired;

    @Column("is_account_non_locked")
    private final boolean isAccountNonLocked;

    @Column("is_credentials_non_expired")
    private final boolean isCredentialsNonExpired;

    @Column("is_enabled")
    private final boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
