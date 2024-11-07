package org.com.wired.application.factory.userFollower;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.infra.persistence.FollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserRepositoryPort;
import org.com.wired.domain.usecase.userFollower.followUser.FollowUserUsecase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Configuration
public class FollowUserFactory {
    private final UserRepositoryPort userRepositoryPort;
    private final UserFollowerRepositoryPort userFollowerRepositoryPort;
    private final FollowerRepositoryPort followerRepositoryPort;

    @Bean("FollowUserUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FollowUserUsecase create() {
        return FollowUserUsecase.builder()
            .userRepositoryPort(userRepositoryPort)
            .followerRepositoryPort(followerRepositoryPort)
            .userFollowerRepositoryPort(userFollowerRepositoryPort)
            .build();
    }
}
