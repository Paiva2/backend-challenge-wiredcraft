package org.com.wired.domain.ports.outbound.infra.persistence;

import org.com.wired.domain.entity.UserFollower;
import org.com.wired.domain.usecase.userFollower.listFollowers.dto.ListUserFollowersPageDTO;

import java.util.Optional;

public interface UserFollowerRepositoryPort {
    Optional<UserFollower> findUserFollowing(Long userId, Long userFollowingId);

    Optional<UserFollower> findUserFollower(Long userId, Long userFollowerId);

    UserFollower persist(UserFollower userFollower);

    void remove(UserFollower userFollower);

    ListUserFollowersPageDTO findUserFollowers(Long userId, int page, int perPage, String followerName, String sort);
}
