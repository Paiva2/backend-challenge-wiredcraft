package org.com.wired.domain.ports.inbound.usecase;

public interface UnfollowUserUsecasePort {
    void execute(Long userId, Long unfollowUserId);
}
