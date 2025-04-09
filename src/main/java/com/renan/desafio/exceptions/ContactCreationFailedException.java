package com.renan.desafio.exceptions;

public class ContactCreationFailedException extends RuntimeException {
    public ContactCreationFailedException(String message) {
        super(message);
    }
}
