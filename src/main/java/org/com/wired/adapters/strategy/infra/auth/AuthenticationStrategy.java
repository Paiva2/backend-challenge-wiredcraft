package org.com.wired.adapters.strategy.infra.auth;

public interface AuthenticationStrategy {
    String createAuth(String subject);

    void verifyAuth(String jwt);
}
