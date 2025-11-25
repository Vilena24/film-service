package com.example.film_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class ExternalApiException extends RuntimeException{

    private final HttpStatusCode status;
    private final String responseBody;
    public ExternalApiException(String message, HttpStatusCode status, String responseBody){
        super(message); //конструктор, просто передает текст ошибки родителю(RuntimeException)
        this.status = status;
        this.responseBody = responseBody;
    }



    public HttpStatusCode getStatus(){
        return status;
    }

    public String getResponseBody(){
        return responseBody;
    }
}
