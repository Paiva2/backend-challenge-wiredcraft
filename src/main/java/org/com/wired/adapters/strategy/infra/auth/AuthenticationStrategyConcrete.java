package org.com.wired.adapters.strategy.infra.auth;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AuthenticationStrategyConcrete {
    private final AuthenticationStrategy authenticationStrategy;

    public String createAuthentication(String subject) {
        return authenticationStrategy.createAuth(subject);
    }

    public void verifyAuthentication(String token) {
        authenticationStrategy.verifyAuth(token);
    }
}
