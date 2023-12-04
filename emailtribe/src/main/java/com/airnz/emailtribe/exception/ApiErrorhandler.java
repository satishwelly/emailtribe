package com.airnz.emailtribe.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class ApiErrorhandler extends ResponseEntityExceptionHandler {
    //Exception handler for 400 errors
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleException(
            final BadRequestException exception, final HttpServletRequest request
    ) {
        var response = new ApiErrorResponse(
                exception.getMessage(),
                exception.getHttpStatus().value(),
                exception.getHttpStatus().name(),
                request.getRequestURI(),
                request.getMethod()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    //Exception handler for 403 errors
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ResponseEntity<?> handleException(
            final ForbiddenException exception, final HttpServletRequest request
    ) {
        var response = new ApiErrorResponse(
                exception.getMessage(),
                exception.getHttpStatus().value(),
                exception.getHttpStatus().name(),
                request.getRequestURI(),
                request.getMethod()
        );
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    //Exception handler for 404 errors
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleException(
            final NotFoundException exception, final HttpServletRequest request
    ) {
        var response = new ApiErrorResponse(
                exception.getMessage(),
                exception.getHttpStatus().value(),
                exception.getHttpStatus().name(),
                request.getRequestURI(),
                request.getMethod()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    //Exception handler for 500 errors
    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleException(
            final InternalServerException exception, final HttpServletRequest request
    ) {
        var response = new ApiErrorResponse(
                exception.getMessage(),
                exception.getHttpStatus().value(),
                exception.getHttpStatus().name(),
                request.getRequestURI(),
                request.getMethod()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
