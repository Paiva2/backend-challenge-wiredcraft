package org.com.wired.application.factory.user;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Configuration
public class FindUserFactory {
    private final UserRepositoryPort userRepositoryPort;

    @Bean("FindUserUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FindUserUsecase create() {
        return FindUserUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .build();
    }
}
