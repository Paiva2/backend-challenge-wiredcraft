package org.com.wired.adapters.utils;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.utils.PasswordUtilsPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordUtilsAdapter implements PasswordUtilsPort {
    private final static Integer MINIMUM_PASSWORD_LENGTH = 6;

    private PasswordEncoder passwordEncoder;

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    @Override
    public boolean hasNecessaryLength(String password) {
        return password.length() >= MINIMUM_PASSWORD_LENGTH;
    }
}
