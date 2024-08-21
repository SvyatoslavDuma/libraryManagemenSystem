package com.community.library.management.exception;

public class MaxLimitException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Member has reached the borrow limit";

    public MaxLimitException(){
        super(DEFAULT_MESSAGE);
    }

    public MaxLimitException(String message){
        super(message);
    }
}
