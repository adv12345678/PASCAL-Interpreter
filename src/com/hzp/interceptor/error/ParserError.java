package com.hzp.interceptor.error;

import com.hzp.interceptor.token.Token;

public class ParserError extends Error {
    public ParserError(String message, Token token, ErrorCode errorCode) {
        super(message, token, errorCode);
    }
}
