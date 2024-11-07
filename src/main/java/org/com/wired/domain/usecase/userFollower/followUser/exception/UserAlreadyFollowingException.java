package org.com.wired.domain.usecase.userFollower.followUser.exception;

import org.com.wired.domain.usecase.common.exception.ConflictException;

import java.text.MessageFormat;

public class UserAlreadyFollowingException extends ConflictException {
    private final static String MESSAGE = "User {0} is already following user {1}.";

    public UserAlreadyFollowingException(String userId, String userFollowingId) {
        super(MessageFormat.format(MESSAGE, userId, userFollowingId));
    }
}
