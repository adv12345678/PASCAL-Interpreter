package com.hzp.interceptor.ast;

import com.hzp.interceptor.token.Token;

import java.util.Objects;

public class Var extends AST {

    private String value;

    public Var(Token token) {
        super(token);
        this.value = (String) token.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Var)) return false;
        Var var = (Var) o;
        return value == var.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
