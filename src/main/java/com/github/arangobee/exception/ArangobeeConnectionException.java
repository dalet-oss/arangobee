package com.github.arangobee.exception;

/**
 * Error while connection to ArangoDB
 *
 * @author lstolowski
 * @since 27/07/2014
 */
public class ArangobeeConnectionException extends ArangobeeException {
    public ArangobeeConnectionException(String message, Exception baseException) {
        super(message, baseException);
    }
}
