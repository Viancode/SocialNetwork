package com.example.socialnetwork.exception;

public class NotFoundException extends ClientErrorException {

    public NotFoundException(String message) {
        super(message);
    }
}
