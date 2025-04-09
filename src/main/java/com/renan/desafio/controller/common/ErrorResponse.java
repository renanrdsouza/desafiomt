package com.renan.desafio.controller.common;

import java.util.List;

public record ErrorResponse(
        String message,
        int statusCode,
        List<FieldError> erros
) {
}
