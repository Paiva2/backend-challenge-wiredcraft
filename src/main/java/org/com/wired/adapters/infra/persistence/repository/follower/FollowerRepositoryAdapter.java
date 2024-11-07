package org.com.wired.adapters.infra.persistence.repository.follower;

import lombok.AllArgsConstructor;
import org.com.wired.adapters.infra.mapper.FollowerMapper;
import org.com.wired.application.infra.persistence.entity.FollowerEntity;
import org.com.wired.domain.entity.Follower;
import org.com.wired.domain.ports.outbound.infra.persistence.FollowerRepositoryPort;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class FollowerRepositoryAdapter implements FollowerRepositoryPort {
    private final FollowerRepositoryOrm repository;

    @Override
    public Follower persist(Follower follower) {
        FollowerEntity followerEntity = repository.save(FollowerMapper.toPersistence(follower));
        return FollowerMapper.toDomain(followerEntity);
    }
}
