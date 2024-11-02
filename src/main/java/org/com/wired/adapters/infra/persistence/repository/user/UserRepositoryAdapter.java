package org.com.wired.adapters.infra.persistence.repository.user;

import lombok.AllArgsConstructor;
import org.com.wired.adapters.infra.mapper.UserMapper;
import org.com.wired.application.infra.persistence.entity.UserEntity;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {
    private final UserRepositoryOrm repository;

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> userEntity = repository.findByEmail(email);
        if (userEntity.isEmpty()) return Optional.empty();
        return Optional.of(UserMapper.toDomain(userEntity.get()));
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserEntity> userEntity = repository.findById(id);
        if (userEntity.isEmpty()) return Optional.empty();
        return Optional.of(UserMapper.toDomain(userEntity.get()));
    }

    @Override
    public User persist(User user) {
        UserEntity userEntity = repository.save(UserMapper.toPersistence(user));
        return UserMapper.toDomain(userEntity);
    }
}
