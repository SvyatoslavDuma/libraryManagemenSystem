package com.community.library.management.exception;

public class BookNotDeletableException extends RuntimeException {

    public BookNotDeletableException(String message) {
        super(message);
    }

}