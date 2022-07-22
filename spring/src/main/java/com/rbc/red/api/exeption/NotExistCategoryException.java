package com.rbc.red.api.exeption;

public class NotExistCategoryException extends RuntimeException{
    public NotExistCategoryException(String message) {
        super(message);
    }
}