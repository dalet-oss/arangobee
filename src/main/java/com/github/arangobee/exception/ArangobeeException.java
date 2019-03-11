package com.github.arangobee.exception;

/**
 * @author abelski
 */
public class ArangobeeException extends Exception {
    public ArangobeeException(String message) {
        super(message);
    }

    public ArangobeeException(String message, Throwable cause) {
        super(message, cause);
    }
}
