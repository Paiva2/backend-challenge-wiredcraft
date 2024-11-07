package org.com.wired.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Follower {
    private Long id;
    private Date createdAt;
    private User user;

    private List<UserFollower> userFollowers;
}
