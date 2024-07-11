package com.example.socialnetwork.exception;

public class DuplicateException extends ClientErrorException {

    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException() {
        super("Duplicate exception");
    }
}
