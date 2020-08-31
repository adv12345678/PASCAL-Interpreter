package com.hzp.interceptor.ast;

import com.hzp.interceptor.token.Token;

public class Num extends AST{
    private Object value;

    public Num(Token token) {
        super(token);
        this.value = token.getValue();
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
