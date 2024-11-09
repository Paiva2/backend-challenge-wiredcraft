package org.com.wired.application.factory.userFollower;

import lombok.AllArgsConstructor;
import org.com.wired.domain.ports.outbound.infra.persistence.FollowerRepositoryPort;
import org.com.wired.domain.ports.outbound.infra.persistence.UserFollowerRepositoryPort;
import org.com.wired.domain.usecase.user.findUserUsecase.FindUserUsecase;
import org.com.wired.domain.usecase.userFollower.followUser.FollowUserUsecase;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@AllArgsConstructor
@Configuration
public class FollowUserFactory {
    private final UserFollowerRepositoryPort userFollowerRepositoryPort;
    private final FollowerRepositoryPort followerRepositoryPort;
    private final FindUserUsecase findUserUsecase;

    @Bean("FollowUserUsecase")
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public FollowUserUsecase create() {
        return FollowUserUsecase.builder()
            .findUserUsecase(findUserUsecase)
            .followerRepositoryPort(followerRepositoryPort)
            .userFollowerRepositoryPort(userFollowerRepositoryPort)
            .build();
    }
}
