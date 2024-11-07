package org.com.wired.application.gateway.controller.userFollower;

import lombok.AllArgsConstructor;
import org.com.wired.application.gateway.output.FollowUserOutput;
import org.com.wired.domain.usecase.userFollower.followUser.FollowUserUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserFollowerControllerImpl implements UserFollowerController {
    private final FollowUserUsecase followUserUsecase;

    @Override
    @Transactional
    public ResponseEntity<FollowUserOutput> followUser(Authentication authentication, Long userToFollowId) {
        Long subjectId = Long.parseLong(authentication.getName());
        FollowUserOutput output = followUserUsecase.execute(subjectId, userToFollowId);
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }
}
