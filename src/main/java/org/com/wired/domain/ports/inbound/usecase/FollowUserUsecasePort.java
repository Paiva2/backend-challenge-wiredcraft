package org.com.wired.domain.ports.inbound.usecase;

import org.com.wired.application.gateway.output.FollowUserOutput;

public interface FollowUserUsecasePort {
    FollowUserOutput execute(Long userId, Long userToFollowId);
}
