package com.tcit.vms.vms.exception;

public class UserNotFoundException  extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
