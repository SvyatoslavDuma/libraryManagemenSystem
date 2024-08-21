package com.community.library.management.exception;

public class MemberNotDeletableException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Member cannot be deleted because they have borrowed books";

    public MemberNotDeletableException(String message) {
        super(message);
    }

    public MemberNotDeletableException() {
        super(DEFAULT_MESSAGE);
    }
}
