package org.com.wired.domain.usecase.userFollower.followUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.application.gateway.output.FollowUserOutput;
import org.com.wired.domain.entity.Follower;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.entity.UserFollower;
import org.com.wired.domain.ports.inbound.usecase.FollowUserUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.FollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.common.exception.UserDisabledException;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;
import org.com.wired.domain.usecase.userFollower.followUser.exception.InvalidFollowException;
import org.com.wired.domain.usecase.userFollower.followUser.exception.UserAlreadyFollowingException;

import java.util.Date;
import java.util.Optional;

//todo: notify user followed
@AllArgsConstructor
@Builder
public class FollowUserUsecase implements FollowUserUsecasePort {
    private final UserRepositoryPort userRepositoryPort;
    private final UserFollowerRepositoryPort userFollowerRepositoryPort;
    private final FollowerRepositoryPort followerRepositoryPort;

    @Override
    public FollowUserOutput execute(Long userId, Long userToFollowId) {
        User user = findUser(userId);
        checkUserDisabled(user);

        User userToFollow = findUser(userToFollowId);
        checkUserDisabled(userToFollow);

        checkUserFollowingValid(user, userToFollow);
        checkUserAlreadyFollow(user, userToFollow);

        Follower followerFilled = fillFollower(user);
        Follower followerPersisted = persistFollower(followerFilled);

        Date dateFollowed = new Date();
        UserFollower userFollower = fillUserFollower(userToFollow, followerPersisted, dateFollowed);
        persistUserFollower(userFollower);
        userFollower.setCreatedAt(dateFollowed);

        return mountOutput(userFollower);
    }

    private User findUser(Long userId) {
        return userRepositoryPort.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private void checkUserDisabled(User user) {
        if (user.getDisabledAt() != null) {
            throw new UserDisabledException(user.getId().toString());
        }
    }

    private void checkUserFollowingValid(User user, User userToFollow) {
        if (user.getId().equals(userToFollow.getId())) {
            throw new InvalidFollowException();
        }
    }

    private void checkUserAlreadyFollow(User user, User userToFollow) {
        Optional<UserFollower> userFollower = userFollowerRepositoryPort.findUserFollowing(user.getId(), userToFollow.getId());

        if (userFollower.isPresent()) {
            throw new UserAlreadyFollowingException(user.getId().toString(), userToFollow.getId().toString());
        }
    }

    private Follower fillFollower(User user) {
        return Follower.builder()
            .user(user)
            .build();
    }

    private Follower persistFollower(Follower follower) {
        return followerRepositoryPort.persist(follower);
    }

    private UserFollower fillUserFollower(User userToFollow, Follower follower, Date dateFollowed) {
        UserFollower.KeyId keyId = new UserFollower.KeyId(userToFollow.getId(), follower.getUser().getId());

        return UserFollower.builder()
            .keyId(keyId)
            .follower(follower)
            .user(userToFollow)
            .createdAt(dateFollowed)
            .build();
    }

    private void persistUserFollower(UserFollower userFollower) {
        userFollowerRepositoryPort.persist(userFollower);
    }

    private FollowUserOutput mountOutput(UserFollower userFollower) {
        return FollowUserOutput.output(userFollower);
    }
}
