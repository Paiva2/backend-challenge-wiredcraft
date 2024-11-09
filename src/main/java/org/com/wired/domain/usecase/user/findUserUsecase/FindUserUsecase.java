package org.com.wired.domain.usecase.user.findUserUsecase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.inbound.usecase.FindUserUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.common.exception.UserDisabledException;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;

@AllArgsConstructor
@Builder
public class FindUserUsecase implements FindUserUsecasePort {
    private final UserRepositoryPort userRepositoryPort;

    public User execute(Long userId) {
        User user = checkUserExists(userId);
        checkUserDisabled(user);

        return user;
    }

    private User checkUserExists(Long id) {
        return userRepositoryPort.findById(id).orElseThrow(UserNotFoundException::new);
    }

    private void checkUserDisabled(User user) {
        if (user.getDisabledAt() != null) {
            throw new UserDisabledException(user.getId().toString());
        }
    }
}
