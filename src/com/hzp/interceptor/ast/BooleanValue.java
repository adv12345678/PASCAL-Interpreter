package com.hzp.interceptor.ast;

import com.hzp.interceptor.token.Token;

public class BooleanValue extends AST{
    private boolean value;
    public BooleanValue(Token token) {
        super(token);
        this.value = (boolean) token.getValue();
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
