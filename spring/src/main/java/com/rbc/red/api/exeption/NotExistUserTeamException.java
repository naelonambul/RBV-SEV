package com.rbc.red.api.exeption;

public class NotExistUserTeamException extends RuntimeException{
    public NotExistUserTeamException(String message) {
        super(message);
    }
}
