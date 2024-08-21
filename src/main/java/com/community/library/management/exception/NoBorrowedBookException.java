package com.community.library.management.exception;

public class NoBorrowedBookException extends  RuntimeException{
    private static final String DEFAULT_MESSAGE = "There are no borrowed books";

    public NoBorrowedBookException(){
        super(DEFAULT_MESSAGE);
    }

    public NoBorrowedBookException(String message){
        super(message);
    }
}
