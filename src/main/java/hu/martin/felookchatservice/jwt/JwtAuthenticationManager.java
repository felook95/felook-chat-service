package hu.martin.felookchatservice.jwt;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;

    public JwtAuthenticationManager(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        return Mono.just(authentication)
                .map(Authentication::getCredentials)
                .map(Object::toString)
                .flatMap(jwtUtil::validateRawToken)
                .map(jwtUtil::getAllClaimsFromToken)
                .onErrorResume(throwable -> Mono.empty())
                .map(claims -> {
                    List<Map<String, String>> authorities = (List<Map<String, String>>) claims.get("authorities");

                    Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                            .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                            .collect(Collectors.toSet());

                    return (Authentication) new UsernamePasswordAuthenticationToken(claims.getSubject(), null, simpleGrantedAuthorities);
                })
                .switchIfEmpty(Mono.just(authentication));

    }
}
