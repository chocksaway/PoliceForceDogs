package com.chocksaway.exception;

/**
 * Exception thrown when a kennel characteristic name provided by the client does not exist.
 */
public class InvalidCharacteristicException extends RuntimeException {

    public InvalidCharacteristicException(String message) {
        super(message);
    }
}

