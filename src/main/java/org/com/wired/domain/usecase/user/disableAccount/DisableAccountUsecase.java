package org.com.wired.domain.usecase.user.disableAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.inbound.usecase.DisableAccountUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.common.exception.UserDisabledException;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;

import java.util.Date;

@AllArgsConstructor
@Builder
public class DisableAccountUsecase implements DisableAccountUsecasePort {
    private final UserRepositoryPort userRepositoryPort;

    @Override
    public void execute(Long id) {
        User user = checkUserExists(id);
        checkUserDisabled(user);

        user.setDisabledAt(new Date());
        userRepositoryPort.persist(user);
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
