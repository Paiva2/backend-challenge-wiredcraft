package org.com.wired.domain.ports.inbound.usecase;

import org.com.wired.domain.usecase.userFollower.listFollowers.dto.ListUserFollowersPageDTO;

public interface ListFollowersUsecasePort {
    ListUserFollowersPageDTO execute(Long userId, Integer page, Integer perPage, String followerName, String sort);
}
