package org.com.wired.domain.usecase.userFollower.unfollowUser.exception;

import org.com.wired.domain.usecase.common.exception.NotFoundException;

public class FollowerNotFoundException extends NotFoundException {
    public FollowerNotFoundException(String message) {
        super(message);
    }
}
