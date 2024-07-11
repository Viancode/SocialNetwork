package com.example.socialnetwork.exception;

public class ConflictException extends ClientErrorException {

    public ConflictException(String message) {
        super(message);
    }
}
