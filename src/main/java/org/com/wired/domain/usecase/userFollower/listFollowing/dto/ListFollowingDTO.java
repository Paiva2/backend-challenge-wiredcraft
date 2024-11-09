package org.com.wired.domain.usecase.userFollower.listFollowing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.wired.adapters.utils.DateUtilsAdapter;
import org.com.wired.domain.entity.UserFollower;
import org.com.wired.domain.ports.outbound.utils.DateUtilsPort;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ListFollowingDTO {
    private int page;
    private int size;
    private Long total;
    private int totalPages;
    private boolean isLast;
    private List<UserFollowingDTO> followers;

    private static final DateUtilsPort dateUtilsPort = new DateUtilsAdapter();

    public static UserFollowingDTO toUserFollowingDTO(UserFollower userFollower) {
        return UserFollowingDTO.builder()
            .followId(userFollower.getFollower().getId())
            .userFollowedId(userFollower.getUser().getId())
            .userFollowedName(userFollower.getUser().getName())
            .userFollowedEmail(userFollower.getUser().getEmail())
            .followingSince(dateUtilsPort.dateTimeToOutputString(userFollower.getCreatedAt()))
            .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class UserFollowingDTO {
        private Long followId;
        private Long userFollowedId;
        private String userFollowedName;
        private String userFollowedEmail;
        private String followingSince;
    }
}
