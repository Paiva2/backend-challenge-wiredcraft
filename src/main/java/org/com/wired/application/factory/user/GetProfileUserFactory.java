package org.com.wired.application.factory.user;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.infra.persistence.AddressRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.user.getProfileUser.GetProfileUserUsecase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Configuration
public class GetProfileUserFactory {
    private final UserRepositoryPort userRepositoryPort;
    private final AddressRepositoryPort addressRepositoryPort;

    @Bean("GetProfileUserUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public GetProfileUserUsecase create() {
        return GetProfileUserUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .addressRepositoryPort(addressRepositoryPort)
            .build();
    }
}
