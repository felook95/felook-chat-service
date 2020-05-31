package hu.martin.felookchatservice.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {
    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(serverWebExchange1 -> Mono.justOrEmpty(serverWebExchange1.getRequest().getCookies().get("jwt")))
                .filter(httpCookies -> !httpCookies.isEmpty())
                .map(httpCookies -> httpCookies.get(0).getValue())
                .map(jwtToken -> new UsernamePasswordAuthenticationToken(jwtToken, jwtToken));
    }
}
