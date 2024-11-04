package org.com.wired.application.factory.user;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.user.disableAccount.DisableAccountUsecase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Configuration
public class DisableAccountFactory {
    private final UserRepositoryPort userRepositoryPort;

    @Bean("DisableAccountUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public DisableAccountUsecase create() {
        return DisableAccountUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .build();
    }
}
