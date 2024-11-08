package org.com.wired.application.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "tb_users_followers")
public class UserFollowerEntity {
    @EmbeddedId
    private UserFollowerEntity.KeyId keyId;

    @MapsId("userId")
    @JoinColumn(name = "ufl_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity user;

    @MapsId("followerId")
    @JoinColumn(name = "ufl_follower_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FollowerEntity follower;

    @JoinColumn(name = "ufl_created_at")
    @CreationTimestamp
    private Date createdAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Embeddable
    public static class KeyId {
        private Long userId;
        private Long followerId;
    }
}
