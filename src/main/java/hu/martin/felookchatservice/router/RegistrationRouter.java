package hu.martin.felookchatservice.router;

import hu.martin.felookchatservice.handler.RegistrationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RegistrationRouter {

    @Bean
    public RouterFunction<ServerResponse> registrationRoute(RegistrationHandler registrationHandler) {
        RouterFunction<ServerResponse> registrationRoutes = route()
                .POST("/", registrationHandler::registerApplicationUser)
                .build();
        return nest(RequestPredicates.path("/registration"), registrationRoutes);
    }
}
