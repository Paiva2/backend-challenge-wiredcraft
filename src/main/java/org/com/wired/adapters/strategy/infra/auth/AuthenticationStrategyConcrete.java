package org.com.wired.adapters.strategy.infra.auth;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@AllArgsConstructor
@Component
public class AuthenticationStrategyConcrete {
    private final AuthenticationStrategy authenticationStrategy;

    public String createAuthentication(String subject) {
        return authenticationStrategy.createAuth(subject);
    }

    public String verifyAuthentication(String token) {
        return authenticationStrategy.verifyAuth(token);
    }

    public List<String> getTokenClaims(String token) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return authenticationStrategy.getClaimRoles(token);
    }
}
