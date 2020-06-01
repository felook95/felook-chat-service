package hu.martin.felookchatservice.security;

import hu.martin.felookchatservice.jwt.JwtConfig;
import hu.martin.felookchatservice.jwt.JwtServerAuthenticationConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@EnableConfigurationProperties(JwtConfig.class)
public class ApplicationSecurityConfig {

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final JwtServerAuthenticationConverter jwtServerAuthenticationConverter;

    public ApplicationSecurityConfig(@Qualifier("jwtReactiveAuthenticationManager") ReactiveAuthenticationManager reactiveAuthenticationManager, JwtServerAuthenticationConverter jwtServerAuthenticationConverter) {
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
        this.jwtServerAuthenticationConverter = jwtServerAuthenticationConverter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(jwtServerAuthenticationConverter);

        return http
                .authorizeExchange()
                .pathMatchers("/registration").permitAll()
                .pathMatchers("/login").permitAll()
                .pathMatchers("/logout").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic().disable()
                .cors().configurationSource(serverWebExchange -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.applyPermitDefaultValues();
                    corsConfig.setAllowedOrigins(Collections.singletonList(serverWebExchange.getRequest().getHeaders().getOrigin()));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }).and()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .build();
    }

}