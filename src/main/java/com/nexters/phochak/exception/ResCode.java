package com.nexters.phochak.exception;

public enum ResCode {

    private final String code;
    private final String message;

    ResCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
