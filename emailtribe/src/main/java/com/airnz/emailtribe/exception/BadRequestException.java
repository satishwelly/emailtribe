package com.airnz.emailtribe.exception;

import org.springframework.http.HttpStatus;
public class BadRequestException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public BadRequestException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
