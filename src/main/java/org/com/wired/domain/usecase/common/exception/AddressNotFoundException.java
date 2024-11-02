package org.com.wired.domain.usecase.common.exception;

public class AddressNotFoundException extends NotFoundException {
    private final static String MESSAGE = "Address not found!";

    public AddressNotFoundException() {
        super(MESSAGE);
    }
}
