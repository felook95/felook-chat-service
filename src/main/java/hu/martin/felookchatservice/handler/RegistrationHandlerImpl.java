package hu.martin.felookchatservice.handler;

import hu.martin.felookchatservice.auth.ApplicationUser;
import hu.martin.felookchatservice.auth.AuthenticationCookieAndResponseProvider;
import hu.martin.felookchatservice.dto.model.ApplicationUserDto;
import hu.martin.felookchatservice.repository.ApplicationUserRepository;
import hu.martin.felookchatservice.security.ApplicationUserRole;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;

@Component
public class RegistrationHandlerImpl implements RegistrationHandler {

    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationCookieAndResponseProvider authenticationCookieAndResponseProvider;

    public RegistrationHandlerImpl(
            ApplicationUserRepository applicationUserRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationCookieAndResponseProvider authenticationCookieAndResponseProvider) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationCookieAndResponseProvider = authenticationCookieAndResponseProvider;
    }

    @Nonnull
    @Override
    public Mono<ServerResponse> registerApplicationUser(ServerRequest request) {
        return request.bodyToMono(ApplicationUserDto.class).flatMap(applicationUserDto ->
                applicationUserRepository.existsByUsername(applicationUserDto.getUsername())
                        .flatMap(isExists -> {
                            if (isExists) {
                                return ServerResponse.status(HttpStatus.CONFLICT).bodyValue("User exists");
                            } else {
                                ApplicationUser applicationUserToSave = getApplicationUserToSaveWithDefaults(applicationUserDto);

                                return applicationUserRepository.save(applicationUserToSave)
                                        .flatMap(authenticationCookieAndResponseProvider::getLoginResponse);

                            }
                        }));

    }

    private ApplicationUser getApplicationUserToSaveWithDefaults(ApplicationUserDto applicationUserDto) {
        return new ApplicationUser()
                .setId(null)
                .setUsername(applicationUserDto.getUsername())
                .setPassword(passwordEncoder.encode(applicationUserDto.getPassword()))
                .setRole(ApplicationUserRole.USER.name())
                .setAccountNonExpired(true)
                .setAccountNonLocked(true)
                .setCredentialsNonExpired(true)
                .setEnabled(true);
    }
}
