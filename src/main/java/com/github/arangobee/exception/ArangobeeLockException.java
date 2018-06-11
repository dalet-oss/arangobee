package com.github.arangobee.exception;

/**
 * Error while can not obtain process lock
 */
public class ArangobeeLockException extends ArangobeeException {
  public ArangobeeLockException(String message) {
    super(message);
  }
}
