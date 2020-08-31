package com.hzp.interceptor.error;

import com.hzp.interceptor.token.Token;

public class Error extends RuntimeException{
    private Token token;
    private ErrorCode errorCode;

    public Error() {
    }

    public Error(String message, Token token, ErrorCode errorCode) {
        super(message);
        this.token = token;
        this.errorCode = errorCode;
    }

    public Error(String message) {
        super(message);
    }
}
