package org.com.wired.domain.usecase.userFollower.listFollowing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;
import org.com.wired.domain.usecase.userFollower.listFollowing.dto.ListFollowingDTO;

@AllArgsConstructor
@Builder
public class ListFollowingUsecase {
    private final FindUserUsecase findUserUsecase;
    private final UserFollowerRepositoryPort userFollowerRepositoryPort;

    public ListFollowingDTO execute(Long userId, int page, int size, String followingName, String direction) {
        User user = findUserUsecase.execute(userId);

        if (page < 1) {
            page = 1;
        }

        if (size < 5) {
            size = 5;
        } else if (size > 50) {
            size = 50;
        }

        ListFollowingDTO followingList = findFollowingList(user.getId(), page, size, followingName, direction);
        return followingList;
    }

    private ListFollowingDTO findFollowingList(Long userId, int page, int size, String followingName, String direction) {
        return userFollowerRepositoryPort.listFollowing(userId, page, size, followingName, direction);
    }
}
