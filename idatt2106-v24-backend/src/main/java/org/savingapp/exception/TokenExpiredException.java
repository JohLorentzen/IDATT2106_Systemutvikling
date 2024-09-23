package org.savingapp.exception;


/**
 * Exception used to symbolize an expired token.
 */
public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException(String message) {
        super(message);
    }
}
