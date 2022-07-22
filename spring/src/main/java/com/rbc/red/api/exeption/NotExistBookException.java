package com.rbc.red.api.exeption;

public class NotExistBookException extends RuntimeException{
    public NotExistBookException(String message) {
        super(message);
    }
}
