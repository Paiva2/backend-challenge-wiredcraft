package org.com.wired.adapters.infra.persistence.repository.userFollower;

import lombok.AllArgsConstructor;
import org.com.wired.adapters.infra.mapper.UserFollowerMapper;
import org.com.wired.application.infra.persistence.entity.UserFollowerEntity;
import org.com.wired.domain.entity.UserFollower;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class UserFollowerRepositoryAdapter implements UserFollowerRepositoryPort {
    private final UserFollowerRepositoryOrm repository;

    @Override
    public Optional<UserFollower> findUserFollowing(Long userId, Long userFollowingId) {
        Optional<UserFollowerEntity> userFollower = repository.findFollowing(userId, userFollowingId);
        if (userFollower.isEmpty()) return Optional.empty();
        return Optional.of(UserFollowerMapper.toDomain(userFollower.get()));
    }

    @Override
    public UserFollower persist(UserFollower userFollower) {
        UserFollowerEntity userFollowerEntity = repository.save(UserFollowerMapper.toPersistence(userFollower));
        return UserFollowerMapper.toDomain(userFollowerEntity);
    }
}
