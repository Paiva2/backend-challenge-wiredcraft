package org.com.wired.domain.usecase.userFollower.listFollowers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.inbound.usecase.ListFollowersUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.common.exception.UserDisabledException;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;
import org.com.wired.domain.usecase.userFollower.listFollowers.dto.ListUserFollowersPageDTO;

@AllArgsConstructor
@Builder
public class ListFollowersUsecase implements ListFollowersUsecasePort {
    private final UserRepositoryPort userRepositoryPort;
    private final UserFollowerRepositoryPort userFollowerRepositoryPort;

    @Override
    public ListUserFollowersPageDTO execute(Long userId, Integer page, Integer perPage, String followerName, String sort) {
        User user = findUser(userId);
        checkUserDisabled(user);

        if (page < 1) {
            page = 1;
        }

        if (perPage < 5) {
            perPage = 5;
        } else if (perPage > 50) {
            perPage = 50;
        }

        return findUserFollowers(user.getId(), page, perPage, followerName, sort);
    }

    private User findUser(Long userId) {
        return userRepositoryPort.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private void checkUserDisabled(User user) {
        if (user.getDisabledAt() != null) {
            throw new UserDisabledException(user.getId().toString());
        }
    }

    private ListUserFollowersPageDTO findUserFollowers(Long userId, int page, int perPage, String followerName, String sort) {
        return userFollowerRepositoryPort.findUserFollowers(userId, page, perPage, followerName, sort);
    }
}
