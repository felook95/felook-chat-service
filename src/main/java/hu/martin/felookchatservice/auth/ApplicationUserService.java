package hu.martin.felookchatservice.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
public class ApplicationUserService implements ReactiveUserDetailsService {

    private final ApplicationUserRepository applicationUserRepository;

    public ApplicationUserService(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return applicationUserRepository
                .findByUsername(username)
                .map(Function.identity());
    }
}
