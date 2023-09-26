package com.tcit.vms.vms.exception;
public class JwtTokenException extends RuntimeException{
    public JwtTokenException() {
    }
    public JwtTokenException(String msg) {
        super(msg);
    }
}

