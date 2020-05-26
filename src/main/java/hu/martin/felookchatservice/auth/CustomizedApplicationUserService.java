package hu.martin.felookchatservice.auth;

import hu.martin.felookchatservice.dto.model.ApplicationUserDto;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

public interface CustomizedApplicationUserService {

    Mono<ServerResponse> registerApplicationUser(ApplicationUserDto applicationUserDto);
}
