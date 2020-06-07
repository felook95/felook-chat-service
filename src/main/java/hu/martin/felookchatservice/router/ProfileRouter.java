package hu.martin.felookchatservice.router;

import hu.martin.felookchatservice.handler.ProfileHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProfileRouter {

    @Bean
    public RouterFunction<ServerResponse> profileRoute(ProfileHandler profileHandler) {
        RouterFunction<ServerResponse> profileRoute = route()
                .GET("/jwt", profileHandler::getProfileByJwtToken)
                .build();

        return nest(RequestPredicates.path("/profile"), profileRoute);
    }
}
