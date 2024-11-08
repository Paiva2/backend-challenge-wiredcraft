package org.com.wired.domain.usecase.userFollower.followUser.exception;

import org.com.wired.domain.usecase.common.exception.BadRequestException;

public class InvalidFollowException extends BadRequestException {
    private final static String MESSAGE = "Invalid follow. Users can't follow themselves.";

    public InvalidFollowException() {
        super(MESSAGE);
    }
}
