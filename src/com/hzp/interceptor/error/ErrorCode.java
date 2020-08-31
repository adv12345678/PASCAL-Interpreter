package com.hzp.interceptor.error;

public enum  ErrorCode {
    UNEXPECTED_TOKEN("unexpected token"),
    ID_NOT_FOUND("identify no found"),
    DUPLICATE_ID("Duplicated id found"),
    PARAMETER_NUMBER_NO_MATCH("parameter number no match");

    public String getValue() {
        return value;
    }

    private String value;

    ErrorCode(String value) {
        this.value = value;
    }
}
