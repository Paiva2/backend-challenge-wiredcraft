package org.com.wired.domain.ports.outbound.infra.persistence;

import org.com.wired.domain.entity.User;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);

    User persist(User user);
}
