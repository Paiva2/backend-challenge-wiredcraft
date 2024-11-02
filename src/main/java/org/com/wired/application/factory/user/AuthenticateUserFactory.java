package org.com.wired.application.factory.user;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.ports.outbound.utils.PasswordUtilsPort;
import org.com.wired.domain.usecase.user.authenticateUser.AuthenticateUserUsecase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Configuration
public class AuthenticateUserFactory {
    private final UserRepositoryPort userRepositoryPort;
    private final PasswordUtilsPort passwordUtilsPort;

    @Bean("AuthenticateUserUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public AuthenticateUserUsecase create() {
        return AuthenticateUserUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .passwordUtilsPort(passwordUtilsPort)
            .build();
    }
}
