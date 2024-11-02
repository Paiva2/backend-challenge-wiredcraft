package org.com.wired.domain.usecase.user.getProfileUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.application.gateway.output.GetProfileUserOutput;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.inbound.GetProfileUserUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.common.exception.AddressNotFoundException;
import org.com.wired.domain.usecase.common.exception.UserDisabledException;
import org.com.wired.domain.usecase.common.exception.UserNotFoundException;

@AllArgsConstructor
@Builder
public class GetProfileUserUsecase implements GetProfileUserUsecasePort {
    private final UserRepositoryPort userRepositoryPort;
    private final AddressRepositoryPort addressRepositoryPort;

    @Override
    public GetProfileUserOutput execute(Long userId) {
        User user = findUser(userId);
        checkUserDisabled(user);

        Address address = findAddress(user.getId());
        user.setAddress(address);

        return mountOutput(user);
    }

    private User findUser(Long userId) {
        return userRepositoryPort.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private void checkUserDisabled(User user) {
        if (user.getDisabledAt() != null) {
            throw new UserDisabledException(user.getId().toString());
        }
    }

    private Address findAddress(Long userId) {
        return addressRepositoryPort.findByUserId(userId).orElseThrow(AddressNotFoundException::new);
    }

    private GetProfileUserOutput mountOutput(User user) {
        return GetProfileUserOutput.output(user);
    }
}
