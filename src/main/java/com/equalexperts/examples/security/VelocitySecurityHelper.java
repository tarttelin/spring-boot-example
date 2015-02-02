package com.equalexperts.examples.security;

import com.equalexperts.examples.model.MyUser;
import com.equalexperts.examples.model.Role;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class VelocitySecurityHelper {

    public Optional<MyUser> loggedInUser() {

        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).flatMap(auth -> {
            Object principal = auth.getPrincipal();
            if (principal instanceof MyUser) {
                return Optional.of((MyUser) principal);
            } else return Optional.empty();
        });

    }

    public boolean hasAuthority(final Role role) {
        return loggedInUser()
                .map(u -> u.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(role.name())))
                .orElse(false);
    }

    public boolean isAdmin() {
        return hasAuthority(Role.ADMIN);
    }
}