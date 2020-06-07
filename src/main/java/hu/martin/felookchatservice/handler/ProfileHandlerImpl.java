package hu.martin.felookchatservice.handler;

import hu.martin.felookchatservice.auth.ApplicationUser;
import hu.martin.felookchatservice.dto.mapper.UserProfileMapper;
import hu.martin.felookchatservice.dto.model.UserProfileDto;
import hu.martin.felookchatservice.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnull;
import java.security.Principal;

@Component
public class ProfileHandlerImpl implements ProfileHandler {

    private final ReactiveUserDetailsService userDetailsService;

    public ProfileHandlerImpl(@Qualifier("applicationUserServiceImpl") ReactiveUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Nonnull
    @Override
    public Mono<ServerResponse> getProfileByJwtToken(ServerRequest serverRequest) {
        return serverRequest
                .principal()
                .map(Principal::getName)
                .flatMap(userDetailsService::findByUsername)
                .cast(ApplicationUser.class)
                .map(applicationUser -> applicationUser.getUser().getUserProfile())
                .map(UserProfileMapper::toUserProfileDto)
                .flatMap(userProfileDto -> ServerResponse.ok().body(Mono.just(userProfileDto), UserProfileDto.class));
    }
}
