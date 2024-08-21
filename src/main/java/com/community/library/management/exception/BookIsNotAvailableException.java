package com.community.library.management.exception;

public class BookIsNotAvailableException extends  RuntimeException{
    private static final String DEFAULT_MESSAGE = "Book is not available for borrowing";

    public BookIsNotAvailableException(){
        super(DEFAULT_MESSAGE);
    }

    public BookIsNotAvailableException(String message){
        super(message);
    }
}
