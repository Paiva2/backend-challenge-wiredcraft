package org.com.wired.domain.usecase.userFollower.unfollowUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.domain.entity.Follower;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.entity.UserFollower;
import org.com.wired.domain.ports.inbound.usecase.UnfollowUserUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.FollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;
import org.com.wired.domain.usecase.userFollower.unfollowUser.exception.FollowerNotFoundException;
import org.com.wired.domain.usecase.userFollower.unfollowUser.exception.UserFollowerNotFoundException;

import java.util.Optional;

@AllArgsConstructor
@Builder
public class UnfollowUserUsecase implements UnfollowUserUsecasePort {
    private final FindUserUsecase findUserUsecase;
    private final UserFollowerRepositoryPort userFollowerRepositoryPort;
    private final FollowerRepositoryPort followerRepositoryPort;

    @Override
    public void execute(Long userId, Long unfollowUserId) {
        User user = findUserUsecase.execute(userId);
        UserFollower userFollower = findUserFollower(unfollowUserId, user.getId());

        if (userFollower.getFollower() == null) {
            throw new FollowerNotFoundException("Follower not found!");
        }

        Follower follower = userFollower.getFollower();

        removeUserFollower(userFollower);
        removeFollower(follower);
    }

    private UserFollower findUserFollower(Long userFollowedId, Long userFollowerId) {
        Optional<UserFollower> userFollower = userFollowerRepositoryPort.findUserFollower(userFollowedId, userFollowerId);

        if (userFollower.isEmpty()) {
            throw new UserFollowerNotFoundException("User " + userFollowedId + " has no follower with id " + userFollowerId);
        }

        return userFollower.get();
    }

    private void removeUserFollower(UserFollower userFollower) {
        userFollowerRepositoryPort.remove(userFollower);
    }

    private void removeFollower(Follower follower) {
        followerRepositoryPort.remove(follower);
    }
}
