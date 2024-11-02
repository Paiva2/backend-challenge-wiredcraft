package org.com.wired.domain.ports.outbound.utils;

public interface PasswordUtilsPort {
    String encode(String password);

    boolean matches(String rawPassword, String hashedPassword);

    boolean hasNecessaryLength(String password);
}
