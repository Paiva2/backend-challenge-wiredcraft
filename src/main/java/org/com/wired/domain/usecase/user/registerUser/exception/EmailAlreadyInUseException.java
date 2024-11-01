package org.com.wired.domain.usecase.user.registerUser.exception;

import org.com.wired.domain.usecase.common.exception.ConflictException;

import java.text.MessageFormat;

public class EmailAlreadyInUseException extends ConflictException {
    private final static String MESSAGE = "Email {0} is already in use.";

    public EmailAlreadyInUseException(String email) {
        super(MessageFormat.format(MESSAGE, email));
    }
}
