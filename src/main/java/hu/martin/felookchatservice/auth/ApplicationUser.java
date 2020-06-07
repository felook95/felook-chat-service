package hu.martin.felookchatservice.auth;

import hu.martin.felookchatservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table("application_user")
public class ApplicationUser implements UserDetails {

    @Id
    @Column("id")
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("password")
    private String password;

    @Column("username")
    private String username;

    @Column("role")
    private String role;

    @Transient
    private Set<? extends GrantedAuthority> grantedAuthorities;

    @Transient
    private User user;

    @Column("is_account_non_expired")
    private boolean isAccountNonExpired;

    @Column("is_account_non_locked")
    private boolean isAccountNonLocked;

    @Column("is_credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @Column("is_enabled")
    private boolean isEnabled;

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
