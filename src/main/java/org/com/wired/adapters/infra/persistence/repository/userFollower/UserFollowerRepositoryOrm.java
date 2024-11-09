package org.com.wired.adapters.infra.persistence.repository.userFollower;

import org.com.wired.application.infra.persistence.entity.UserFollowerEntity;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Description("Find User (userId) follow in other User (userFollowingId)")
    Optional<UserFollowerEntity> findUserFollowing(@Param("userId") Long userId, @Param("userFollowingId") Long userFollowingId);

    @Query("SELECT uf FROM UserFollowerEntity uf " +
        "WHERE uf.user.id = :userId " +
        "AND uf.follower.user.id = :userFollowingId")
    @Description("Find User (userId) follower (userFollowerId)")
    Optional<UserFollowerEntity> findUserFollower(@Param("userId") Long userId, @Param("userFollowingId") Long userFollowerId);

    @Query(value = "SELECT * FROM tb_users_followers uf " +
        "JOIN tb_users usr ON usr.usr_id = uf.ufl_user_id " +
        "JOIN tb_followers fol ON fol.fol_id = uf.ufl_follower_id " +
        "JOIN tb_users ufol ON ufol.usr_id = fol.fol_user_id " +
        "WHERE usr.usr_id = :userId " +
        "AND (:followerName IS NULL OR lower(ufol.usr_name) LIKE concat('%', lower(:followerName), '%'))", nativeQuery = true)
    Page<UserFollowerEntity> findUserFollowers(Long userId, String followerName, Pageable pageable);
}
