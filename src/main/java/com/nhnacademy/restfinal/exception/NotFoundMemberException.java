package com.nhnacademy.restfinal.exception;

public class NotFoundMemberException extends RuntimeException {
    public NotFoundMemberException(String message) {
        super(message);
    }
}
