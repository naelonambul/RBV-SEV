package com.rbc.red.api.exeption;

public class NotExistUserException extends RuntimeException{
    public NotExistUserException(String message) {
        super(message);
    }
}
