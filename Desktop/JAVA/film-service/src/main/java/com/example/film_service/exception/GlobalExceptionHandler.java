package com.example.film_service.exception;


import com.example.film_service.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<?> handleExternalApiException(ExternalApiException e) {

        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                e.getStatus() != null ? e.getStatus().value() : 0,
                e.getResponseBody()
        );
        return  ResponseEntity
                .status(e.getStatus() != null ? e.getStatus().value() : 500)
                .body(response);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException e){
        return ResponseEntity
                .status(400)
                .body(Map.of("message", e.getMessage()));
    }



}
