package hu.martin.felookchatservice.controller;

import hu.martin.felookchatservice.auth.ApplicationUserService;
import hu.martin.felookchatservice.auth.ApplicationUserServiceImpl;
import hu.martin.felookchatservice.dto.model.ApplicationUserDto;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final ApplicationUserService applicationUserService;

    public RegistrationController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

    @PostMapping
    public Mono<ServerResponse> registerUser(@RequestBody ApplicationUserDto applicationUserDto) {
        return applicationUserService.registerApplicationUser(applicationUserDto);
    }

}
