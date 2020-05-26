package hu.martin.felookchatservice.auth;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

public interface ApplicationUserService extends ReactiveUserDetailsService, CustomizedApplicationUserService {
}
