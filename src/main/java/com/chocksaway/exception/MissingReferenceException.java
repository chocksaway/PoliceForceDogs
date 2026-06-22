package com.chocksaway.exception;

/**
 * Exception thrown when a referenced entity id provided by the client does not
 * exist in the database
 */
public class MissingReferenceException extends RuntimeException {

    public MissingReferenceException(String message) {
        super(message);
    }
}

