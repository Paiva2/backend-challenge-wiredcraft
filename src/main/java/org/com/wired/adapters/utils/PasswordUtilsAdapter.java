package org.com.wired.adapters.utils;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.utils.PasswordUtilsPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordUtilsAdapter implements PasswordUtilsPort {
    private PasswordEncoder passwordEncoder;

    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
