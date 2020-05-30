package hu.martin.felookchatservice.router;

import hu.martin.felookchatservice.handler.LoginHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class LoginRouter {

    @Bean
    public RouterFunction<ServerResponse> loginRoute(LoginHandler loginHandler) {
        return route().POST("/login", loginHandler::loginApplicationUser)
                .GET("/login", loginHandler::getJwtToken)
                .build();
    }

}
