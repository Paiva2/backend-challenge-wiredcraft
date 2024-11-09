package org.com.wired.domain.usecase.user.getProfileUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.com.wired.application.gateway.output.GetProfileUserOutput;
import org.com.wired.domain.entity.Address;
import org.com.wired.domain.entity.User;
import org.com.wired.domain.ports.inbound.usecase.GetProfileUserUsecasePort;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.com.wired.domain.usecase.common.exception.AddressNotFoundException;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;

@AllArgsConstructor
@Builder
public class GetProfileUserUsecase implements GetProfileUserUsecasePort {
    private final AddressRepositoryPort addressRepositoryPort;
    private final FindUserUsecase findUserUsecase;

    @Override
    public GetProfileUserOutput execute(Long userId) {
        User user = findUserUsecase.execute(userId);

        Address address = findAddress(user.getId());
        user.setAddress(address);

        return mountOutput(user);
    }

    private Address findAddress(Long userId) {
        return addressRepositoryPort.findByUserId(userId).orElseThrow(AddressNotFoundException::new);
    }

    private GetProfileUserOutput mountOutput(User user) {
        return GetProfileUserOutput.output(user);
    }
}
