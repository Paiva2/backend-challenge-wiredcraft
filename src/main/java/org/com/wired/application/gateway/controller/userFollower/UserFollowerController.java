package org.com.wired.application.gateway.controller.userFollower;

import org.com.wired.application.gateway.output.FollowUserOutput;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/follow")
public interface UserFollowerController {
    @PostMapping("/user/{userToFollowId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    ResponseEntity<FollowUserOutput> followUser(Authentication authentication, @PathVariable("userToFollowId") Long userToFollowId);
}
