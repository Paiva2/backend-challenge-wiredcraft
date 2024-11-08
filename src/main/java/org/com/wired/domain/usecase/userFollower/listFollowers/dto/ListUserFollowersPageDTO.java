package org.com.wired.domain.usecase.userFollower.listFollowers.dto;

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
public class ListUserFollowersPageDTO {
    private int page;
    private int size;
    private Long total;
    private int totalPages;
    private boolean isLast;
    private List<UserFollowersDto> followers;

    private static final DateUtilsPort dateUtilsPort = new DateUtilsAdapter();

    public static UserFollowersDto toUserFollowersDto(UserFollower userFollower) {
        return UserFollowersDto.builder()
            .userId(userFollower.getFollower().getUser().getId())
            .followerId(userFollower.getFollower().getId())
            .name(userFollower.getFollower().getUser().getName())
            .email(userFollower.getFollower().getUser().getEmail())
            .followedAt(dateUtilsPort.dateTimeToOutputString(userFollower.getCreatedAt()))
            .build();
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class UserFollowersDto {
        private Long userId;
        private Long followerId;
        private String name;
        private String email;
        private String followedAt;
    }
}
