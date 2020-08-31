package com.hzp.interceptor.error;

import com.hzp.interceptor.token.Token;

public class SemanticError extends Error {
    public SemanticError(String message, Token token, ErrorCode errorCode) {
        super(message, token, errorCode);
    }
}
