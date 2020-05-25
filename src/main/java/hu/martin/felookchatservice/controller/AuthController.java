package hu.martin.felookchatservice.controller;

import hu.martin.felookchatservice.auth.ApplicationUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AuthController {

    private final ApplicationUserService applicationUserService;

    public AuthController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @GetMapping("/{username}")
    public Mono<UserDetails> getUser(@PathVariable String username) {
        return applicationUserService.findByUsername(username);
    }
}
