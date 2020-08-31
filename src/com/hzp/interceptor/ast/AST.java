package com.hzp.interceptor.ast;

import com.hzp.interceptor.token.Token;

public class AST {
    protected Token token;

    public AST(Token token) {
        this.token = token;
    }


    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
}
