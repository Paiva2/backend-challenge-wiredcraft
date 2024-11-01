package org.com.wired.domain.usecase.common.exception;

import java.text.MessageFormat;

public class InvalidPropertyException extends BadRequestException {
    public static final String MESSAGE = "Invalid property. Reason: {0}";

    public InvalidPropertyException(String message) {
        super(MessageFormat.format(MESSAGE, message));
    }
}
