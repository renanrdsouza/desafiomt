package com.renan.desafio.controller.common;

import com.renan.desafio.exceptions.RefreshTokenNotFoundException;
import com.renan.desafio.exceptions.TokenAcquisitionException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TokenAcquisitionException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleTokenAcquisitionException(TokenAcquisitionException ex) {
        log.error("Token acquisition failed: {}", ex.getMessage());

        return new ErrorResponse(
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                List.of()
        );
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ErrorResponse handleRateLimitException(RequestNotPermitted ex) {
        log.error("Rate limit exceeded: {}", ex.getMessage());

        return new ErrorResponse(
                "Rate limit exceeded. Please try again later.",
                HttpStatus.TOO_MANY_REQUESTS.value(),
                List.of()
        );
    }

    @ExceptionHandler(RefreshTokenNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleRefreshTokenNotFoundException(RefreshTokenNotFoundException ex) {
        log.error("Refresh token not found: {}", ex.getMessage());

        return new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                List.of()
        );
    }

    @ExceptionHandler(HttpClientErrorException.Conflict.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleHttpClientErrorExceptionConflict(HttpClientErrorException.Conflict ex) {
        log.error("Conflict error occurred: {}", ex.getMessage());

        return new ErrorResponse(
                "Contact already exists with the provided email.",
                HttpStatus.CONFLICT.value(),
                List.of()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage());

        return new ErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                List.of()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception ex) {
        log.error("An unexpected error occurred: {}", ex.getMessage());

        return new ErrorResponse(
                "An unexpected error occurred. Please contact support.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                List.of()
        );
    }
}
