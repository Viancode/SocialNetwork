package com.example.socialnetwork.exception.custom;

import org.springframework.security.access.AccessDeniedException;

public class NotAllowException extends AccessDeniedException {
    public NotAllowException(String message) {
        super(message);
    }
}
