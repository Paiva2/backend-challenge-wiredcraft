package org.com.wired.domain.usecase.common.exception;

import java.text.MessageFormat;

public class UserDisabledException extends ForbiddenException {
    private final static String MESSAGE = "User with identifier {0} is disabled!";

    public UserDisabledException(String id) {
        super(MessageFormat.format(MESSAGE, id));
    }
}
