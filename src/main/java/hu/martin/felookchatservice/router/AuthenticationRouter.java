package hu.martin.felookchatservice.router;

import hu.martin.felookchatservice.handler.AuthenticationHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class AuthenticationRouter {

    @Bean
    public RouterFunction<ServerResponse> authenticationRoute(AuthenticationHandler authenticationHandler) {
        return route().POST("/login", authenticationHandler::loginApplicationUser)
                .POST("/logout", authenticationHandler::logoutApplicationUser)
                .build();
    }

}
