package com.hzp.interceptor.error;

public class LexerError extends Error{
    private int lineNumber;
    private int columnNumber;

    public LexerError(String message) {
        super(message);
    }
}
