package com.airnz.emailtribe.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public NotFoundException(String message, HttpStatus httpStatus) {
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
