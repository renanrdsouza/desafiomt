package com.renan.desafio.util;

import com.renan.desafio.exceptions.InvalidSignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Sha256SignatureValidator implements SignatureValidator{

    @Value("${hubspot.client.secret}")
    private String clientSecret;

    @Override
    public boolean isValid(String payload, String signature) {
        try {
            String testString = clientSecret + payload;

            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = algorithm.digest(testString.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString().equals(signature);
        } catch (NoSuchAlgorithmException e) {
            throw new InvalidSignatureException("Error validating signature");
        }
    }
}
