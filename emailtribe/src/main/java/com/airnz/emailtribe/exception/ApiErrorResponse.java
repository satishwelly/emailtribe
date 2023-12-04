package com.airnz.emailtribe.exception;

public class ApiErrorResponse {
    private String message;
    private Integer statusCode;
    private String errorCode;
    private String path;
    private String method;

    public ApiErrorResponse(String message, int statusCode, String errorCode, String path, String method) {
        this.message = message;
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.path = path;
        this.method = method;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
