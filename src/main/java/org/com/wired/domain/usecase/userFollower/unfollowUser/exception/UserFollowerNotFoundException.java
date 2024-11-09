package org.com.wired.domain.usecase.userFollower.unfollowUser.exception;

import org.com.wired.domain.usecase.common.exception.NotFoundException;

public class UserFollowerNotFoundException extends NotFoundException {
    public UserFollowerNotFoundException(String message) {
        super(message);
    }
}
