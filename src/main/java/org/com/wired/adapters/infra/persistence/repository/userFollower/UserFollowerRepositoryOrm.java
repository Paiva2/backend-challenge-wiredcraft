package org.com.wired.adapters.infra.persistence.repository.userFollower;

import org.com.wired.application.infra.persistence.entity.UserFollowerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFollowerRepositoryOrm extends JpaRepository<UserFollowerEntity, UserFollowerEntity.KeyId> {
    @Query("SELECT uf FROM UserFollowerEntity uf " +
        "WHERE uf.follower.user.id = :userId " +
        "AND uf.user.id = :userFollowingId")
    Optional<UserFollowerEntity> findFollowing(@Param("userId") Long userId, @Param("userFollowingId") Long userFollowingId);
}
