package com.zosh.exception;

public class AddressException extends RuntimeException {

    public AddressException() {
        super();
    }

    public AddressException(String message) {
        super(message);
    }
}
