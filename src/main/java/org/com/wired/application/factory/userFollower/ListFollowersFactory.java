package org.com.wired.application.factory.userFollower;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.userFollower.listFollowers.ListFollowersUsecase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Configuration
public class ListFollowersFactory {
    private final UserRepositoryPort userRepositoryPort;
    private final UserFollowerRepositoryPort userFollowerRepositoryPort;

    @Bean("ListFollowersUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ListFollowersUsecase create() {
        return ListFollowersUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .userFollowerRepositoryPort(userFollowerRepositoryPort)
            .build();
    }
}
