package org.com.wired.application.gateway.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.wired.adapters.utils.DateUtilsAdapter;
import org.com.wired.domain.entity.UserFollower;
import org.com.wired.domain.ports.outbound.utils.DateUtilsPort;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FollowUserOutput {
    private Long userFollowedId;
    private String followedAt;

    private static final DateUtilsPort dateUtils = new DateUtilsAdapter();

    public static FollowUserOutput output(UserFollower userFollower) {
        return FollowUserOutput.builder()
            .userFollowedId(userFollower.getUser().getId())
            .followedAt(dateUtils.dateToOutputString(userFollower.getCreatedAt()))
            .build();
    }
}
