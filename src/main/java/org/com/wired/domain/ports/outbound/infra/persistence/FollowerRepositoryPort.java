package org.com.wired.domain.ports.outbound.infra.persistence;

import org.com.wired.domain.entity.Follower;

public interface FollowerRepositoryPort {
    Follower persist(Follower follower);
}
