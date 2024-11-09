package org.com.wired.application.gateway.controller.userFollower;

import org.com.wired.application.gateway.output.FollowUserOutput;
import org.com.wired.domain.usecase.userFollower.listFollowers.dto.ListUserFollowersPageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/follow")
public interface UserFollowerController {
    @PostMapping("/user/{userToFollowId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    ResponseEntity<FollowUserOutput> followUser(Authentication authentication, @PathVariable("userToFollowId") Long userToFollowId);

    @GetMapping("/user/list")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    ResponseEntity<ListUserFollowersPageDTO> listFollowers(Authentication authentication, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page, @RequestParam(name = "size", required = false, defaultValue = "5") Integer size, @RequestParam(name = "followerName", required = false) String followerName, @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort);

    @DeleteMapping("/remove/{userFollowedId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    ResponseEntity<Void> unfollowUser(Authentication authentication, @PathVariable("userFollowedId") Long userFollowedId);
}
