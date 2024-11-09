package org.com.wired.domain.usecase.user.disableAccount;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.inbound.usecase.DisableAccountUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;

import java.util.Date;

@AllArgsConstructor
@Builder
public class DisableAccountUsecase implements DisableAccountUsecasePort {
    private final UserRepositoryPort userRepositoryPort;
    private final FindUserUsecase findUserUsecase;

    @Override
    public void execute(Long id) {
        User user = findUserUsecase.execute(id);

        user.setDisabledAt(new Date());
        userRepositoryPort.persist(user);
    }
}
