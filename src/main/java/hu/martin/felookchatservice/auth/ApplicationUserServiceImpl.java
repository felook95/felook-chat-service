package hu.martin.felookchatservice.auth;

import hu.martin.felookchatservice.dto.mapper.ApplicationUserMapper;
import hu.martin.felookchatservice.dto.model.ApplicationUserDto;
import hu.martin.felookchatservice.security.ApplicationUserRole;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@Log4j2
public class ApplicationUserServiceImpl implements ApplicationUserService {

    private final ApplicationUserRepository applicationUserRepository;

    private final PasswordEncoder passwordEncoder;

    public ApplicationUserServiceImpl(ApplicationUserRepository applicationUserRepository, PasswordEncoder passwordEncoder) {
        this.applicationUserRepository = applicationUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return applicationUserRepository
                .findByUsername(username)
                .map(Function.identity());
    }

    @Override
    public Mono<ServerResponse> registerApplicationUser(ApplicationUserDto applicationUserDto) {
        applicationUserRepository.existsById(2L);
        return null;
//        return Mono.just(true)
//                .flatMap(isExists -> {
//                    if (isExists) {
//                        return ServerResponse.status(HttpStatus.CONFLICT).bodyValue("User exists");
//                    } else {
//                        ApplicationUser applicationUserToSave =
//                                new ApplicationUser()
//                                        .setId(null)
//                                        .setUsername(applicationUserDto.getUsername())
//                                        .setPassword(passwordEncoder.encode(applicationUserDto.getPassword()))
//                                        .setRole(ApplicationUserRole.USER.name())
//                                        .setAccountNonExpired(true)
//                                        .setAccountNonLocked(true)
//                                        .setCredentialsNonExpired(true)
//                                        .setEnabled(applicationUserDto.isEnabled());
//
//                        Mono<ApplicationUser> applicationUserMono = applicationUserRepository.save(applicationUserToSave);
//                        return applicationUserMono.map(ApplicationUserMapper::toApplicationUserDto)
//                                .flatMap(applicationUserDto1 ->
//                                        ServerResponse.ok().body(applicationUserDto1, ApplicationUserDto.class));
//
//                    }
//                });

//
//        return applicationUserRepository
//                .findByUsername(applicationUserDto.getUsername())
//                .switchIfEmpty(Mono.defer(() -> {
//                    Mono<ApplicationUser> applicationUserMono = applicationUserRepository.save(applicationUserToSave);
//                    return applicationUserMono.map(ApplicationUserMapper::toApplicationUserDto);
//                }));
    }
}