package org.com.wired.adapters.strategy.infra.auth;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public interface AuthenticationStrategy {
    String createAuth(String subject);

    String verifyAuth(String jwt);

    List<String> getClaimRoles(String jwt) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
