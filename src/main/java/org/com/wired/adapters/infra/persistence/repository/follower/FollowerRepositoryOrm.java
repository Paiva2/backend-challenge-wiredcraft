package org.com.wired.adapters.infra.persistence.repository.follower;

import org.com.wired.application.infra.persistence.entity.FollowerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowerRepositoryOrm extends JpaRepository<FollowerEntity, Long> {
}
