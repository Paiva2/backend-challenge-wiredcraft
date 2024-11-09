package org.com.wired.domain.usecase.userFollower.unfollowUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.domain.ports.inbound.usecase.UnfollowUserUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.FollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;

@AllArgsConstructor
@Builder
public class UnfollowUserUsecase implements UnfollowUserUsecasePort {
    private final UserRepositoryPort userRepositoryPort;
    private final UserFollowerRepositoryPort userFollowerRepositoryPort;
    private final FollowerRepositoryPort followerRepositoryPort;

    @Override
    public void execute(Long userId, Long unfollowUserId) {
        
    }
}
