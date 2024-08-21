package com.community.library.management.exception;

public class BookNotFoundException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "Book with this ID not found";

    public BookNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
