package org.com.wired.domain.ports.outbound.infra.persistence;

import org.com.wired.domain.entity.User;
import org.com.wired.domain.usecase.user.listUsers.dto.ListUsersPageDTO;

import java.util.Optional;

public interface UserRepositoryPort {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    User persist(User user);

    ListUsersPageDTO listUsersPage(int page, int size, String name);
}
