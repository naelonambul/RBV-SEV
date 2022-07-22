package com.rbc.red.api.exeption;

public class NotExistTeamException extends RuntimeException{
    public NotExistTeamException(String message) {
        super(message);
    }
}
