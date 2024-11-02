package org.com.wired.domain.usecase.common.exception;

public class UserNotFoundException extends NotFoundException {
    private final static String MESSAGE = "User not found!";

    public UserNotFoundException() {
        super(MESSAGE);
    }
}
