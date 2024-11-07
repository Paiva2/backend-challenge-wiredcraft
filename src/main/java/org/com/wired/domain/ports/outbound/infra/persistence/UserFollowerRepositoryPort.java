package org.com.wired.domain.ports.outbound.infra.persistence;

import org.com.wired.domain.entity.UserFollower;

import java.util.Optional;

public interface UserFollowerRepositoryPort {
    Optional<UserFollower> findUserFollowing(Long userId, Long userFollowingId);

    UserFollower persist(UserFollower userFollower);
}
