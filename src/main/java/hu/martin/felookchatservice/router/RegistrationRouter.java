package hu.martin.felookchatservice.router;

import hu.martin.felookchatservice.handler.RegistrationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RegistrationRouter {

    @Bean
    public RouterFunction<ServerResponse> registrationRoute(RegistrationHandler registrationHandler) {
        return route()
                .POST("/registration", registrationHandler::registerApplicationUser)
                .build();
    }
}
