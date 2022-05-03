package com.rbc.red.api.exeption;

public class NotExistGroupAssetException extends RuntimeException{
    public NotExistGroupAssetException(String message) {
        super(message);
    }
}