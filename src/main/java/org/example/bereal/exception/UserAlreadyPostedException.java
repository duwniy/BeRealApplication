package org.example.bereal.exception;

public class UserAlreadyPostedException extends RuntimeException {
    public UserAlreadyPostedException(String message) {
        super(message);
    }
}