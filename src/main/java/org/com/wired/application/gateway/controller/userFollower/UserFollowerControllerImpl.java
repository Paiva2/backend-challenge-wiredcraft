package org.com.wired.application.gateway.controller.userFollower;

import lombok.AllArgsConstructor;
import org.com.wired.application.gateway.output.FollowUserOutput;
import org.com.wired.domain.usecase.userFollower.followUser.FollowUserUsecase;
import org.com.wired.domain.usecase.userFollower.listFollowers.ListFollowersUsecase;
import org.com.wired.domain.usecase.userFollower.listFollowers.dto.ListUserFollowersPageDTO;
import org.com.wired.domain.usecase.userFollower.listFollowing.ListFollowingUsecase;
import org.com.wired.domain.usecase.userFollower.listFollowing.dto.ListFollowingDTO;
import org.com.wired.domain.usecase.userFollower.unfollowUser.UnfollowUserUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserFollowerControllerImpl implements UserFollowerController {
    private final FollowUserUsecase followUserUsecase;
    private final UnfollowUserUsecase unfollowUserUsecase;
    private final ListFollowersUsecase listFollowersUsecase;
    private final ListFollowingUsecase listFollowingUsecase;

    @Override
    @Transactional
    public ResponseEntity<FollowUserOutput> followUser(Authentication authentication, Long userToFollowId) {
        Long subjectId = parseSubjectId(authentication);
        FollowUserOutput output = followUserUsecase.execute(subjectId, userToFollowId);
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<ListUserFollowersPageDTO> listFollowers(Authentication authentication, Integer page, Integer size, String followerName, String sort, Integer maxKmDistance) {
        Long subjectId = parseSubjectId(authentication);
        ListUserFollowersPageDTO output = listFollowersUsecase.execute(subjectId, page, size, followerName, sort, maxKmDistance);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ListFollowingDTO> listFollowing(Authentication authentication, Integer page, Integer size, String followingName, String sort, Integer maxDistanceKm) {
        Long subjectId = parseSubjectId(authentication);
        ListFollowingDTO output = listFollowingUsecase.execute(subjectId, page, size, followingName, sort, maxDistanceKm);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> unfollowUser(Authentication authentication, Long userFollowedId) {
        Long subjectId = parseSubjectId(authentication);
        unfollowUserUsecase.execute(subjectId, userFollowedId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Long parseSubjectId(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }
}
