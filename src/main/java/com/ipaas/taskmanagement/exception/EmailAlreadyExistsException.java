package com.ipaas.taskmanagement.exception;

/**
 * Exception thrown when trying to create a user with an email that already exists.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
