package com.hzp.interceptor.ast;

import com.hzp.interceptor.token.Token;

public class Type extends AST {
    private String value;
    public Type(Token token) {
        super(token);
        this.value = (String) token.getValue();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
