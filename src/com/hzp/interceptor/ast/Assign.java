package com.hzp.interceptor.ast;

import com.hzp.interceptor.token.Token;

import java.util.Objects;

public class Assign extends AST {

    private Var left;
    private AST right;
    public Assign(Var left, Token token, AST right) {
        super(token);
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assign assign = (Assign) o;
        return Objects.equals(left, assign.left) &&
                Objects.equals(right, assign.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }


    public Var getLeft() {
        return left;
    }

    public void setLeft(Var left) {
        this.left = left;
    }

    public AST getRight() {
        return right;
    }

    public void setRight(AST right) {
        this.right = right;
    }
}
