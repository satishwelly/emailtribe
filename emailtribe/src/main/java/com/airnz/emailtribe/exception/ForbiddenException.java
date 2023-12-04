package com.airnz.emailtribe.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ForbiddenException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
