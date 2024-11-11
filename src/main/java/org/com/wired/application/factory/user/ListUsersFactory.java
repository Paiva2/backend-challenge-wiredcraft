package org.com.wired.application.factory.user;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.user.listUsers.ListUsersUsecase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Configuration
public class ListUsersFactory {
    private final UserRepositoryPort userRepositoryPort;

    @Bean("ListUsersUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ListUsersUsecase create() {
        return ListUsersUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .build();
    }
}
