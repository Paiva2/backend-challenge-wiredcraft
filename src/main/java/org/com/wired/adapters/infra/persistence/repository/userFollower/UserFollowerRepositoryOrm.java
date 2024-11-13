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
        "JOIN tb_address usra ON usra.adr_user_id = usr.usr_id " +
        "JOIN tb_followers fol ON fol.fol_id = uf.ufl_follower_id " +
        "JOIN tb_users folu ON folu.usr_id =  fol.fol_user_id " +
        "JOIN tb_address folua ON folua.adr_user_id = folu.usr_id " +
        "WHERE fol.fol_user_id = :userId " +
        "AND (:maxDistanceKm IS NULL OR (ST_DistanceSphere(ST_MakePoint(usra.adr_longitude, usra.adr_latitude), ST_MakePoint(folua.adr_longitude, folua.adr_latitude)) / 1000) <= :maxDistanceKm) " +
        "AND (:followingName IS NULL OR lower(usr.usr_name) LIKE concat('%', lower(:followingName), '%'))", nativeQuery = true)
    @Description("Find All UserFollowerEntity that contains Follower with userId. eg. Get who User (userId) is following")
    Page<UserFollowerEntity> findUserFollowersByFollowUserId(@Param("userId") Long userId, @Param("followingName") String followingName, @Param("maxDistanceKm") Integer maxDistanceKm, Pageable pageable);

    @Query(value = "SELECT * FROM tb_users_followers uf " +
        "JOIN tb_users usr ON usr.usr_id = uf.ufl_user_id " +
        "JOIN tb_address usra ON usra.adr_user_id = usr.usr_id " +
        "JOIN tb_followers fol ON fol.fol_id = uf.ufl_follower_id " +
        "JOIN tb_users ufol ON ufol.usr_id = fol.fol_user_id " +
        "JOIN tb_address ufola ON ufola.adr_user_id = ufol.usr_id " +
        "WHERE usr.usr_id = :userId " +
        "AND (:maxKmDistance IS NULL OR (ST_DistanceSphere(ST_MakePoint(usra.adr_longitude, usra.adr_latitude), ST_MakePoint(ufola.adr_longitude, ufola.adr_latitude)) / 1000) <= :maxKmDistance) " +
        "AND (:followerName IS NULL OR lower(ufol.usr_name) LIKE concat('%', lower(:followerName), '%'))", nativeQuery = true)
    Page<UserFollowerEntity> findUserFollowers(@Param("userId") Long userId, @Param("followerName") String followerName, @Param("maxKmDistance") Integer maxKmDistance, Pageable pageable);
}
