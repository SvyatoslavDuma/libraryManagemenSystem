package com.community.library.management.exception;

public class MemberNotFoundException extends RuntimeException{
    private static final String DEFAULT_MESSAGE = "Member not found";
    public MemberNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
    public MemberNotFoundException(String message){super (message);}
}
