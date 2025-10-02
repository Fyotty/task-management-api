package com.ipaas.taskmanagement.exception;

/**
 * Exception thrown when a subtask is not found.
 */
public class SubtaskNotFoundException extends RuntimeException {

    public SubtaskNotFoundException(String message) {
        super(message);
    }
}
