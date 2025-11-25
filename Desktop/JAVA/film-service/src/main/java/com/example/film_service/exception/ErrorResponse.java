package com.example.film_service.exception;

public class ErrorResponse {
    private final String message;
    private final int status;
    private final String externalResponse;


    public ErrorResponse(String message, int status, String externalResponse) {
        this.message = message;
        this.status = status;
        this.externalResponse = externalResponse;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getExternalResponse() {
        return externalResponse;
    }
}
