package org.com.wired.domain.usecase.user.authenticateUser.exception;

import org.com.wired.domain.usecase.common.exception.ForbiddenException;

public class WrongCredentialsException extends ForbiddenException {
    private static final String MESSAGE = "Wrong credentials!";

    public WrongCredentialsException() {
        super(MESSAGE);
    }
}
