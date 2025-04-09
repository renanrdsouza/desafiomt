package com.renan.desafio.util;

public interface SignatureValidator {
    boolean isValid(String payload, String signature);
}