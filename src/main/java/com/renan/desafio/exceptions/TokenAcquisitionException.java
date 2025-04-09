package com.renan.desafio.exceptions;

public class TokenAcquisitionException extends RuntimeException {
    public TokenAcquisitionException(String message) {
        super(message);
    }

    public TokenAcquisitionException(String message, Exception ex) {
        super(message);
    }
}
