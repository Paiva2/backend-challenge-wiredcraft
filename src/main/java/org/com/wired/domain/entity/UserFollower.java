package org.com.wired.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserFollower {
    private KeyId keyId;
    private User user;
    private Follower follower;
    private Date createdAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class KeyId {
        private Long userId;
        private Long followerId;
    }
}
