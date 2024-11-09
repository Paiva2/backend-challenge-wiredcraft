package org.com.wired.application.factory.userFollower;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.infra.persistence.FollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;
import org.com.wired.domain.usecase.userFollower.unfollowUser.UnfollowUserUsecase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Configuration
public class UnfollowUserFactory {
    private final FindUserUsecase findUserUsecase;
    private final UserFollowerRepositoryPort userFollowerRepositoryPort;
    private final FollowerRepositoryPort followerRepositoryPort;

    @Bean("UnfollowUserUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UnfollowUserUsecase create() {
        return UnfollowUserUsecase.builder()
            .findUserUsecase(findUserUsecase)
            .userFollowerRepositoryPort(userFollowerRepositoryPort)
            .followerRepositoryPort(followerRepositoryPort)
            .build();
    }
}
