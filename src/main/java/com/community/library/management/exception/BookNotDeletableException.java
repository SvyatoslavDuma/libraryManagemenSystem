package com.community.library.management.exception;

public class BookNotDeletableException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Book cannot be deleted because it is borrowed";

    public BookNotDeletableException(){
        super(DEFAULT_MESSAGE);
    }

    public BookNotDeletableException(String message){
        super(message);
    }
}